import { z } from "zod";

const stringToNumber = (v) => {
    if (typeof v === "string" && v.trim() !== "") return Number(v);
    return v;
};

const idSchema = z.string().trim().min(1, "ID sản phẩm là bắt buộc");
const nameSchema = z.string().trim().min(1, "Tên sản phẩm không được để trống").max(255);
const authorSchema = z.string().trim().min(1).max(255);
const descSchema = z.string().trim().min(1);

const priceSchema = z.preprocess(
    stringToNumber,
    z.number({
        invalid_type_error: "Giá phải là số",
    })
        .refine((v) => !Number.isNaN(v), "Giá không hợp lệ")
        .nonnegative("Giá phải >= 0")
);


const quantitySchema = z.preprocess(
    stringToNumber,
    z.number({
        invalid_type_error: "Số lượng phải là số",
    }).int()
        .refine((v) => !Number.isNaN(v), "Số lượng không hợp lệ")
        .nonnegative("Số lượng phải là số nguyên >= 0")
);

export const createSellerProductSchema = z.object({
    body: z.object({
        name: nameSchema,
        author_name: authorSchema,
        description: descSchema,
        price: priceSchema,
        quantity: quantitySchema,
        category_id: idSchema,
    }).strip(),
});

export const updateSellerProductSchema = z.object({
    params: z
        .object({
            id: idSchema,
        })
        .strict(),
    body: z.object({
        name: nameSchema.optional(),
        author_name: authorSchema.optional(),
        description: descSchema.optional(),
        price: priceSchema.optional(),
        quantity: quantitySchema.optional(),
        category_id: idSchema.optional(),
    }).refine(
        (data) => Object.values(data).some((value) => value !== undefined),
        { message: "Phải có ít nhất một trường để cập nhật" }
    ).strip(),
});

export const deleteSellerProductSchema = z.object({
    params: z
        .object({
            id: idSchema,
        })
        .strict(),
});

const orderStatusEnum = z.enum([
    "PENDING",
    "CONFIRMED",
    "PROCESSING",
    "READY_FOR_PICKUP",
    "SHIPPING",
    "DELIVERED",
    "CANCELLED",
    "RETURN_REQUESTED",
    "RETURNED",
    "REFUNDED",
]);


export const updateSellerOrderSchema = z.object({
    params: z
        .object({
            id: idSchema,
        })
        .strict(),
    body: z.object({
        status: orderStatusEnum,
    }).strip()
})
