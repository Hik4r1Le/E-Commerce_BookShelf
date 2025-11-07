import express from "express";
import { getReview, addReview, updateReview, deleteReview } from "../../controllers/review.controller.js";
import authMiddleware from "../../middlewares/auth.middleware.js";
import { validate } from "../../middlewares/validate.middleware.js"
import { findReviewSchema, createReviewSchema, updateReviewSchema, deleteReviewSchema } from "../../validations/schemas/review.schema.js"

const router = express.Router();

router.get("/:id", validate(findReviewSchema), getReview)
router.post("/:id", validate(createReviewSchema), authMiddleware, addReview);
router.patch("/:id", validate(updateReviewSchema), authMiddleware, updateReview);
router.delete("/:id", validate(deleteReviewSchema), authMiddleware, deleteReview);

export default router;