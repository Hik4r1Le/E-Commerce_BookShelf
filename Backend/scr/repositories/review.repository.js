import { prisma } from "../config/prisma.config.js"
import { queryBuilder } from "../utils/query.util.js";

export const findManyReview = async (filter, option, orderBy, skip, take) =>
    await prisma.review.findMany(queryBuilder(filter, option, null, orderBy, skip, take));

export const createReview = async (userId, productId, rating, comment) =>
    await prisma.$transaction(async (tx) => {

        // 1. Tạo review
        const review = await tx.review.create({
            data: {
                user_id: userId,
                product_id: productId,
                rating,
                comment,
            },
        });

        // 2. Aggregate lại toàn bộ review của product
        const aggregate = await tx.review.aggregate({
            where: {
                product_id: productId,
                deleted_at: null, // rất quan trọng nếu dùng soft delete
            },
            _count: {
                rating: true,
            },
            _avg: {
                rating: true,
            },
        });

        const ratingCount = aggregate._count.rating;
        const ratingAvg = aggregate._avg.rating ?? 0;

        // 3. Update product
        await tx.product.update({
            where: { id: productId },
            data: {
                rating_count: ratingCount,
                rating_avg: ratingAvg,
            },
        });

        return review;
    });

export const updateReview = async (userId, reviewId, rating, comment) =>
    await prisma.$transaction(async (tx) => {

        // 1. Lấy review hiện tại (để biết product_id)
        const review = await tx.review.findFirst({
            where: {
                id: reviewId,
                user_id: userId,
                deleted_at: null,
            },
            select: {
                id: true,
                product_id: true,
            },
        });

        if (!review) {
            const error = new Error("Review not found or not authorized");
            error.statusCode = 404;
            throw error;
        }    

        // 2. Update review
        const updatedReview = await tx.review.update({
            where: { id: reviewId },
            data: {
                rating,
                comment,
            },
        });

        // 3. Aggregate lại toàn bộ review của product
        const aggregate = await tx.review.aggregate({
            where: {
                product_id: review.product_id,
                deleted_at: null,
            },
            _count: {
                rating: true,
            },
            _avg: {
                rating: true,
            },
        });

        const ratingCount = aggregate._count.rating;
        const ratingAvg = aggregate._avg.rating ?? 0;

        // 4. Update product
        await tx.product.update({
            where: { id: review.product_id },
            data: {
                rating_count: ratingCount,
                rating_avg: ratingAvg,
            },
        });

        return updatedReview;
    });

export const deleteReview = async (filter) =>
    await prisma.review.delete(queryBuilder(filter));

