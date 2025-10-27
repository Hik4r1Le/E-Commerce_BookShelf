import express from "express"
import AuthController from "../../controllers/auth.controller.js";
import passport from "passport";
import authMiddleware from "../../middlewares/auth.middleware.js"

const router = express.Router();

router.post("/login", AuthController.loginWithEmail);

// Redirect user tới trang Google Login 
router.get("/oauth/google", passport.authenticate("google", {
    scope: ["profile", "email"],
    prompt: "select_account"
}));
// Nhận request từ google kèm theo code, code này dùng để lấy token và profile của user
router.get("/google/callback", passport.authenticate("google", { session: false, failureRedirect: "/api/v1/auth/failure" }), AuthController.handleGoogleCallback);
router.get("/failure", (req, res) => res.status(401).json({ ok: false, message: "Authentication failed" }));

router.post("/register", AuthController.registerWithEmail);
router.post("/verify-otp", AuthController.verifyOtp);
router.post("/otp/resend", AuthController.resendOtp);
router.post("/forgot-password", AuthController.handleForgotPassword);
router.post("/reset-password", authMiddleware, AuthController.resetPassword);

// router.post(/logout);
// router.post("/token/refresh");

export default router;
