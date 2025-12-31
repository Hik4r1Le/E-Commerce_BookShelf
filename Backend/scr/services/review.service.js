import { findManyReview, createReview, updateReview, deleteReview } from "../repositories/review.repository.js";

export const createReviewDetail = async (userId, productId, rating, comment) => 
    await createReview(
        {
            user_id: userId,
            product_id: productId,
            rating,
            comment,
        }
    );

export const findReviewByProductId = async (productId, skip, take) => 
    await findManyReview(
        {
            product_id: productId,
        },
        {
            id: true,
            username: true,
            rating: true,
            comment: true,
            user: {
                select: {
                    username: true
                }
            }
        },
        null,
        skip,
        take
    )

export const updateReviewDetail = async (userId, reviewId, rating, comment) => 
    await updateReview(
        {
            user_id: userId,
            id: reviewId,
        },
        {
            rating,
            comment,
        }
    );

export const deleteReviewDetail = async (userId, reviewId) => 
    await deleteReview(
        {
            user_id: userId,
            id: reviewId
        }
    );

