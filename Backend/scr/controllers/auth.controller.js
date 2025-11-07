import * as authService from "../services/auth.service.js"

export const loginWithEmail = async (req, res, next) => {
    try {
        const { email, password } = req.validated.body;
        const token = await authService.loginWithEmail(email, password);
        res.status(200).json({ success: true, data: { token } });
    } catch (err) {
        next(err);
    }
}

export const handleGoogleCallback = async (req, res, next) => {
    try {
        const token = req.user.token;
        res.status(200).json({ success: true, data: { token } });
    } catch (err) {
        next(err);
    }
}

export const registerWithEmail = async (req, res, next) => {
    try {
        const { email, username, password } = req.validated.body;
        const newUser = await authService.registerWithEmail(email, username, password);
        if (newUser) {
            if (newUser.google_id)
                res.status(201).json({ success: true, data: { newUser, is_account_verified: true } });
            else
                res.status(201).json({ success: true, data: { newUser, is_account_verified: false } });
        }
        else {
            const err = new Error("User registration failed");
            err.statusCode = 400;
            throw err;
        }
    } catch (err) {
        next(err);
    }
}

export const verifyOtp = async (req, res, next) => {
    try {
        const { email, otp, type } = req.validated.body;
        const { isSuccessVerify, token = null } = await authService.verifyOtp(email, otp, type);
        if (isSuccessVerify)
            res.status(200).json({ success: true, data: { is_verified: true, token } });
        else
            res.status(400).json({ success: false, data: { message: "Otp not valid or expired", is_verified: false } });
    } catch (error) {
        next(error);
    }
}

export const resendOtp = async (req, res, next) => {
    try {
        const { email, type } = req.validated.body;
        await authService.sendOrResendOtp(email, type);
        res.status(200).json({ success: true });
    } catch (error) {
        next(error);
    }
}

export const handleForgotPassword = async (req, res, next) => {
    try {
        const { email } = req.validated.body;
        await authService.handleBeforeResetPassword(email);
        res.status(200).json({ success: true });
    } catch (error) {
        next(error);
    }
}

export const resetPassword = async (req, res, next) => {
    try {
        const { password } = req.validated.body;
        const email = req.user?.email;
        await authService.resetPasswordForUser(email, password);
        res.status(201).json({ success: true });
    } catch (error) {
        next(error);
    }
}


