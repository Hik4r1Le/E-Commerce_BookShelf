import { z } from "zod";

const idSchema = z
    .string({
        required_error: "Thiếu ID",
        invalid_type_error: "ID phải là chuỗi",
    })
    .trim()
    .min(1, "ID không được để trống")
    .max(255, "ID không hợp lệ");

const ratingSchema = z
    .preprocess((v) => {
        if (typeof v === "string" && v.trim() !== "") return Number(v);
        return v;
    }, z.number({
        required_error: "Thiếu đánh giá (rating)",
        invalid_type_error: "Rating phải là số",
    })
        .min(1, "Đánh giá tối thiểu là 1 sao")
        .max(5, "Đánh giá tối đa là 5 sao"));

const commentSchema = z
    .string({
        required_error: "Thiếu nội dung đánh giá",
        invalid_type_error: "Comment phải là chuỗi",
    })
    .trim()
    .min(1, "Nội dung đánh giá không được để trống")
    .max(2000, "Nội dung đánh giá quá dài (tối đa 2000 ký tự)");

export const createReviewSchema =
    z.object({
        params: z
            .object({
                id: idSchema.describe("ID của sản phẩm"),
            })
            .strip(),
        body: z
            .object({
                rating: ratingSchema,
                comment: commentSchema,
            })
            .strip(),
    });

export const updateReviewSchema =
    z.object({
        params: z
            .object({
                id: idSchema.describe("ID của review"),
            })
            .strip(),
        body: z
            .object({
                rating: ratingSchema.optional(),
                comment: commentSchema.optional(),
            })
            .refine(
                (data) => data.rating !== undefined || data.comment !== undefined,
                { message: "Phải có ít nhất một trường để cập nhật" }
            )
            .strip(),
    });

export const deleteReviewSchema =
    z.object({
        params: z
            .object({
                id: idSchema.describe("ID của review"),
            })
            .strip(),
    });

export const findReviewSchema =
    z.object({
        params: z
            .object({
                id: idSchema.describe("ID của sản phẩm"),
            })
            .strip(),

        query: z
            .object({
                skip: z
                    .preprocess(
                        (v) => (v === undefined ? 0 : Number(v)),
                        z.number().int().nonnegative().default(0)
                    ),

                take: z
                    .preprocess(
                        (v) => (v === undefined ? 10 : Number(v)),
                        z.number().int().positive().max(100).default(5)
                    ),
            }).strip(),
    });