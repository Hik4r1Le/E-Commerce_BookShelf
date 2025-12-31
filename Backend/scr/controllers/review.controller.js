import { findReviewByProductId, createReviewDetail, updateReviewDetail, deleteReviewDetail } from "../services/review.service.js";

export const addReview = async (req, res, next) => {
    try {
        const userId = req.user.id;
        const productId = req.validated.params.id;
        const { rating, comment } = req.validated.body;
        await createReviewDetail(userId, productId, rating, comment);
        res.status(201).json({ success: true });
    } catch (error) {
        next(error);
    }
}

export const getReview = async (req, res, next) => {
    try {
        const productId = req.validated.params.id;
        const { skip, take } = req.validated.query;
        const reviews = await findReviewByProductId(productId, skip, take);
        res.status(200).json({ success: true, data: reviews });
    } catch (error) {
        next(error);
    }
}

export const updateReview = async (req, res, next) => {
    try {
        const reviewId = req.validated.params.id;
        const userId = req.user.id;
        const { rating, comment } = req.validated.body;
        await updateReviewDetail(userId, reviewId, rating, comment);
        res.status(201).json({ success: true });
    } catch (error) {
        next(error);
    }
}

export const deleteReview = async (req, res, next) => {
    try {
        const reviewId = req.validated.params.id;
        const userId = req.user.id;
        await deleteReviewDetail(userId, reviewId);
        res.status(204).json({ success: true });
    } catch (error) {
        next(error);
    }
}
