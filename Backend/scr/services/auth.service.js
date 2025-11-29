import { createUser, findUser, updateUser } from "../repositories/user.repository.js"
import { createOrUpdateOtp, findOtp, deleteOtp, checkOtpIsExpired } from "../repositories/otp.repository.js"
import { generateAccessToken, hashStringByBcrypt, compareByBcrypt } from "../utils/auth.util.js"
import { sendOtpByEmail } from "../utils/email.utils.js"
import { OAuth2Client } from 'google-auth-library';

export const loginWithEmail = async (email, password) => {
    const user = await findUser({ email });

    if (!user) {
        const error = new Error("User not exist");
        error.statusCode = 404;
        throw error;
    }

    if (!await compareByBcrypt(password, user.password_hash)) {
        const error = new Error("Password not valid");
        error.statusCode = 403;
        throw error;
    }

    if (!user.is_email_verified) {
        const error = new Error("User is not verified");
        error.statusCode = 403;
        throw error;
    }

    return generateAccessToken(user);
}

export const loginWithGoogleFromMobile = async (idToken) => {
    const GOOGLE_CLIENT_ID = process.env.GOOGLE_CLIENT_ID_ANDROID;
    const client = new OAuth2Client(GOOGLE_CLIENT_ID);

    const ticket = await client.verifyIdToken({
        idToken,
        audience: GOOGLE_CLIENT_ID,
    });

    const payload = ticket.getPayload();
    const profile = {
        id: payload.sub,
        emails: [{ value: payload.email, verified: payload.email_verified }],
        displayName: payload.name,
        photos: [{ value: payload.picture }],
        _json: { email_verified: payload.email_verified }
    };

    return await loginOrRegisterWithGoogle(profile);
}

export const loginOrRegisterWithGoogle = async (profile) => {
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
    let user = await findUser({ email });
    if (user) {
        if (!user.google_id) {
            user = await updateUser(
                { email },
                {
                    id: true,
                    email: true
                },
                { google_id: googleId, is_email_verified: true });
        }
    } else {
        user = await createUser({
            email,
            username,
            google_id: googleId,
            is_email_verified: true,
            profile: {
                create: {
                    avatar_url: avatar
                }
            }
        });
    }
    return generateAccessToken(user);
}

export const registerWithEmail = async (email, username, password) => {
    const user = await findUser({ email });
    const hashPassword = await hashStringByBcrypt(password);

    if (user) {
        if (user.password_hash) {
            const error = new Error("Email or username is used");
            error.statusCode = 409;
            throw error;
        }
        if (user.google_id) {
            return await updateUser(
                { email },
                {
                    id: true,
                    email: true
                },
                { password_hash: hashPassword, username, is_email_verified: true }
            );
        }
    }

    const newUser = await createUser(
        {
            email,
            username,
            password_hash: hashPassword,
            is_email_verified: false,
            profile: {
                create: {}
            }
        },
        {
            id: true,
            email: true,
        }
    );
    await sendOrResendOtp(email, "VERIFY_EMAIL");

    return newUser;
}

export const verifyOtp = async (email, otp, type) => {
    console.log(otp);
    console.log(typeof otp);
    const otpData = await findOtp({ email, type });

    if (!otpData) {
        const error = new Error("Otp not exist or not sent to you");
        error.statusCode = 404;
        throw error;
    }
    if (!await compareByBcrypt(otp, otpData.otp_hash)) return { isSuccessVerify: false };

    // Kiểm tra OTP hết hạn
    const isOtpExpired = await checkOtpIsExpired({ email, type });
    if (!isOtpExpired) return { isSuccessVerify: false }

    let result = {};

    if (type === "VERIFY_EMAIL") {
        await updateUser(
            { email },
            {
                id: true,
                email: true
            },
            { is_email_verified: true });
        result = {
            isSuccessVerify: true
        }
    }
    if (type === "RESET_PASSWORD") {
        const temp_token = generateAccessToken({
            email
        }, "10m");
        result = {
            isSuccessVerify: true,
            token: temp_token
        }
    }

    await deleteOtp({ email, type });

    return result;
}

export const sendOrResendOtp = async (email, type) => {
    const otp = Math.floor(100000 + Math.random() * 900000).toString();
    await createOrUpdateOtp(
        { email, type },
        {
            email,
            otp_hash: await hashStringByBcrypt(otp),
            type
        });
    await sendOtpByEmail(otp, email);
}

export const handleBeforeResetPassword = async (email) => {
    const user = await findUser({ email });
    if (!user) {
        const error = new Error("Email not exist");
        error.statusCode = 404;
        throw error;
    }

    if (!user.password_hash) {
        const error = new Error("Your account is created with Google, please login with Google");
        error.statusCode = 409;
        throw error;
    }

    await sendOrResendOtp(email, "RESET_PASSWORD");
}

export const resetPasswordForUser = async (email, newPassword) => {
    if (!email) {
        const error = new Error("Email not exist");
        error.statusCode = 400;
        throw error;
    }
    const user = await findUser({ email });
    if (!user) {
        const error = new Error("User not exist");
        error.statusCode = 404;
        throw error;
    }

    await updateUser(
        { email },
        {
            id: true,
            email: true
        },
        { password_hash: await hashStringByBcrypt(newPassword) }
    )
}

