import express from "express";
import authMiddleware from "../../middlewares/auth.middleware.js"
import { getReviewCheckout } from "../../controllers/checkout.controller.js";
import { validate } from "../../middlewares/validate.middleware.js"
import { getReviewCheckoutSchema } from "../../validations/schemas/checkout.schema.js"

const router = express.Router();

router.post("/review", validate(getReviewCheckoutSchema), authMiddleware, getReviewCheckout);

export default router;
