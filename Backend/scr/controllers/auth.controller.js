import AuthService from "../services/auth.service.js"

class AuthController {
    async loginWithEmail(req, res, next) {
        try {
            const token = await AuthService.loginWithEmail(req.body);
            res.status(200).json({ success: true, data: { token } });
        } catch (err) {
            next(err);
        }
    }

    async handleGoogleCallback(req, res, next) {
        try {
            const token = req.user.token;
            res.status(200).json({ success: true, data: { token } });
        } catch (err) {
            next(err);
        }
    }

    async registerWithEmail(req, res, next) {
        try {
            const newUser = await AuthService.registerWithEmail(req.body);
            if (newUser.id) {
                if (newUser.google_id)
                    res.status(201).json({ success: true, data: { newUser, is_account_verified: true } });
                else 
                    res.status(201).json({ success: true, data: { newUser, is_account_verified: false } });
            }
            else {
                const err = new Error("User registration failed silently");
                err.statusCode = 400;
                throw err;
            }
        } catch (err) {
            next(err);
        }
    }

    async verifyOtp(req, res, next) {
        try {
            const { isSuccessVerify, token = null } = await AuthService.verifyOtp(req.body);
            if (isSuccessVerify) 
                res.status(201).json({ success: true, data: { is_verified: true, token } });
            else
                res.status(400).json({ success: false, data: { message: "Otp not valid or expired", is_verified: false } });
        } catch (error) {
            next(error);
        }
    }

    async resendOtp(req, res, next) {
        try {
            const { email, type } = req.body;
            await AuthService.sendOrResendOtp(email, type);
            res.status(200).json({ success: true });
        } catch (error) {
            next(error);
        }
    }

    async handleForgotPassword(req, res, next) {
        try {
            const { email } = req.body;
            await AuthService.handleBeforeResetPassword(email);
            res.status(200).json({ success: true });
        } catch (error) {
            next(error);
        }
    }

    async resetPassword(req, res, next) {
        try {
            const { password } = req.body;
            await AuthService.resetPasswordForUser(req, password);
            res.status(201).json({ success: true });
        } catch (error) {
            next(error);
        }
    }
}

export default new AuthController();
