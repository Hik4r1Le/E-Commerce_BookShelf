import UserRepo from "../repositories/user.repository.js"
import OtpRepo from "../repositories/otp.repository.js"
import AuthUtil from "../utils/auth.util.js"
import EmailUtil from "../utils/email.utils.js"

class AuthService {
    async loginWithEmail(loginInfo) {
        const { email, password } = loginInfo;
        const user = await UserRepo.findUser({ email });

        if (!user) {
            const error = new Error("User does not exists");
            error.statusCode = 404;
            throw error;
        }

        if (!await AuthUtil.compareByBcrypt(password, user.password_hash)) {
            const error = new Error("Password is not valid");
            error.statusCode = 403;
            throw error;
        }

        if (!user.is_email_verified) {
            const error = new Error("User is not verified");
            error.statusCode = 403;
            throw error;
        }

        return AuthUtil.generateAccessToken(user);
    }

    async loginOrRegisterWithGoogle(profile) {
        const emailObj = (profile.emails && profile.emails[0]) || null;
        if (!emailObj) {
            const error = new Error("Google profile has no email");
            error.statusCode = 400;
            throw new error;
        }

        const email = emailObj.value;
        const googleId = profile.id;
        const avatar = profile.photos[0]?.value || null;
        const emailVerified = emailObj.verified || profile._json?.email_verified || false;
        const username = profile.displayName;

        // SECURITY: nếu yêu cầu email phải verified để tự động link
        if (!emailVerified) {
            const error = new Error("Email not verified by Google");
            error.statusCode = 403;
            throw new error;
        }

        // Tìm user xem có tồn tại hay không
        let user = await UserRepo.findUser({ email });
        if (user) {
            if (!user.google_id) {
                user = await UserRepo.updateUser({ email }, { google_id: googleId, is_email_verified: true, avatar });
            }
        } else {
            user = await UserRepo.createUser({
                email,
                username,
                google_id: googleId,
                is_email_verified: true,
                avatar
            });
        }
        return AuthUtil.generateAccessToken(user);
    }

    async registerWithEmail(signupInfo) {
        const { email, username, password } = signupInfo;
        const user = await UserRepo.findUser({ email });
        const hashPassword = await AuthUtil.hashStringByBcrypt(password);

        if (user) {
            if (user.password_hash) {
                const error = new Error("Email or username is used");
                error.statusCode = 409;
                throw error;
            }
            if (user.google_id) {
                return await UserRepo.updateUser(
                    { email },
                    { password_hash: hashPassword, username, is_email_verified: true }
                );
            }
        }

        const userInfo = {
            email,
            username,
            password_hash: hashPassword,
            is_email_verified: false,
        }
        const newUser = await UserRepo.createUser(userInfo);
        await this.sendOrResendOtp(newUser.email, "verify_email");

        return newUser;
    }

    async verifyOtp(otpInfo) {
        const { email, otp, type } = otpInfo;
        const otpData = await OtpRepo.findOtp({ email, type });

        if (!otpData) {
            const error = new Error("Otp not exists or not sent to you");
            error.statusCode = 404;
            throw error;
        }
        if (!await AuthUtil.compareByBcrypt(otp, otpData.otp_hash)) return { isSuccessVerify: false };

        // Kiểm tra OTP hết hạn
        const currentTime = Date.now();
        const otpCreatedTime = new Date(otpData.updatedAt).getTime();
        const OTP_EXPIRE_TIME = 10 * 60 * 1000;
        if (currentTime - otpCreatedTime > OTP_EXPIRE_TIME) return { isSuccessVerify: false }

        let result = {};

        if (type === "verify_email") {
            await UserRepo.updateUser({ email }, { is_email_verified: true });
            result = {
                isSuccessVerify: true
            }
        }
        if (type === "reset_password") {
            const temp_token = AuthUtil.generateAccessToken({
                email
            }, "10m");
            result = {
                isSuccessVerify: true,
                token: temp_token
            }
        }

        await OtpRepo.deleteOtp(otpData._id);
        return result;
    }

    async sendOrResendOtp(email, type) {
        const otp = Math.floor(100000 + Math.random() * 900000).toString();
        await OtpRepo.createOrUpdateOtp(
            { email, type },
            {
                email,
                otp_hash: await AuthUtil.hashStringByBcrypt(otp),
                type
            });
        await EmailUtil.sendOtpByEmail(otp, email);
    }

    async handleBeforeResetPassword(email) {
        const user = await UserRepo.findUser({ email });
        if (!user) {
            const error = new Error("Email is not exists");
            error.statusCode = 404;
            throw error;
        }

        if (!user.password_hash) {
            const error = new Error("Your account is created with Google, please login with Google");
            error.statusCode = 409;
            throw error;
        }

        await this.sendOrResendOtp(user.email, "reset_password");
    }

    async resetPasswordForUser(request, newPassword) {
        const email = request.user?.email;
        if (!email) {
            const error = new Error("Email not exist");
            error.statusCode = 400;
            throw error;
        }
        const user = await UserRepo.findUser({ email });
        if (!user) {
            const error = new Error("Email is not exists");
            error.statusCode = 404;
            throw error;
        }

        await UserRepo.updateUser(
            { email },
            { password_hash: await AuthUtil.hashStringByBcrypt(newPassword) }
        )
    }
}

export default new AuthService();
