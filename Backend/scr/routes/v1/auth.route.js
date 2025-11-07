import express from "express"
import * as authController from "../../controllers/auth.controller.js";
import passport from "passport";
import authMiddleware from "../../middlewares/auth.middleware.js"
import { validate } from "../../middlewares/validate.middleware.js"
import { loginSchema, registerSchema, forgotSchema, resetSchema } from "../../validations/schemas/user.schema.js"
import { resendOtpSchema, verifyOtpSchema } from "../../validations/schemas/otp.schema.js"

const router = express.Router();

router.post("/login", validate(loginSchema), authController.loginWithEmail);

// Redirect user tới trang Google Login 
router.get("/oauth/google", passport.authenticate("google", {
    scope: ["profile", "email"],
    prompt: "select_account"
}));
// Nhận request từ google kèm theo code, code này dùng để lấy token và profile của user
router.get("/google/callback", passport.authenticate("google", { session: false, failureRedirect: "/api/v1/auth/failure" }), authController.handleGoogleCallback);
router.get("/failure", (req, res) => res.status(401).json({ ok: false, message: "Authentication failed" }));

router.post("/register", validate(registerSchema), authController.registerWithEmail);
router.post("/verify-otp", validate(verifyOtpSchema), authController.verifyOtp);
router.post("/otp/resend", validate(resendOtpSchema), authController.resendOtp);
router.post("/forgot-password", validate(forgotSchema), authController.handleForgotPassword);
router.post("/reset-password", validate(resetSchema), authMiddleware, authController.resetPassword);

// router.post(/logout);
// router.post("/token/refresh");

export default router;
