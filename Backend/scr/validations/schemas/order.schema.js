import { z } from "zod";

/**
 * Hỗ trợ cả MongoDB ObjectId (24 hex) và UUID v4
 */
const objectIdRegex = /^[a-fA-F0-9]{24}$/;
const uuidRegex =
    /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;

/**
 * Preprocess cho body:
 * - Nếu client gửi JSON string (vd: '[{...}]'), parse JSON
 * - Nếu client gửi single object -> convert thành array [obj]
 * - Nếu đã là array -> giữ nguyên
 */
const parseBodyPreprocess = (value) => {
    if (value === undefined || value === null) return undefined;

    // Nếu là array rồi
    if (Array.isArray(value)) return value;

    // Nếu là string -> try parse as JSON
    if (typeof value === "string") {
        const s = value.trim();
        if (s === "") return undefined;
        try {
            const parsed = JSON.parse(s);
            // parsed should be array or object
            if (Array.isArray(parsed)) return parsed;
            if (typeof parsed === "object" && parsed !== null) return [parsed];
        } catch (e) {
            // Not JSON -> cannot safely parse into array of objects
            return undefined;
        }
    }

    // If it's an object -> wrap in array
    if (typeof value === "object") return [value];

    return undefined;
};

/**
 * Schema cho 1 item trong order body
 * Fields:
 * - address_id (required): id của address (user đã lưu)
 * - coupon_id (optional): id của coupon (nếu có)
 * - shipping_method_id (required): id của phương thức giao hàng
 * - stock_id (required): id của stock (sản phẩm cụ thể)
 * - quantity (required): số lượng, int >=1
 * - total_price (required): tổng giá tiền cho item (sau shipping/coupon) >=0
 *
 * Lưu ý: backend nên tính lại total_price dựa trên dữ liệu chính xác từ DB để tránh client gian lận.
 */

/**
 * .refine((val) => objectIdRegex.test(val) || uuidRegex.test(val), {
        message: "Field id phải là MongoDB ObjectId (24 hex) hoặc UUID hợp lệ",
    })
 */
const idStringSchema = z
    .string()
    .trim()
    .min(1);

const quantitySchema = z.preprocess(
    (v) => {
        if (typeof v === "string" && v.trim() !== "") return Number(v);
        return v;
    },
    z
        .number({
            invalid_type_error: "quantity phải là số",
            required_error: "quantity là bắt buộc",
        })
        .int("quantity phải là số nguyên")
        .min(1, "quantity tối thiểu là 1")
        .max(1000, "quantity quá lớn")
);

const totalPriceSchema = z.preprocess(
    (v) => {
        if (typeof v === "string" && v.trim() !== "") return Number(v);
        return v;
    },
    z
        .number({
            invalid_type_error: "total_price phải là số",
            required_error: "total_price là bắt buộc",
        })
        .nonnegative("total_price phải >= 0")
);

/**
 * .refine((val) => {
                if (val === undefined) return true;
                return objectIdRegex.test(val) || uuidRegex.test(val);
            }, "coupon_id phải là ObjectId hoặc UUID nếu được cung cấp")
 */

const orderItemSchema = z
    .object({
        address_id: idStringSchema,
        coupon_id: z
            .string()
            .trim()
            .optional(),
        shipping_method_id: idStringSchema,
        stock_id: idStringSchema,
        quantity: quantitySchema,
        total_price: totalPriceSchema,
    })
    .strip();

/**
 * createOrderSchema:
 * - body: array of orderItemSchema
 * - require at least 1 item, max 100 items
 */
export const createOrderSchema =
    z.object({
        body: z.object({
            order: z
                .preprocess(parseBodyPreprocess, z.array(orderItemSchema))
                .refine((arr) => Array.isArray(arr) && arr.length > 0, {
                    message: "Body phải là mảng ít nhất 1 item",
                })
                .superRefine((arr, ctx) => {
                    if (!Array.isArray(arr)) return;
                    if (arr.length > 100) {
                        ctx.addIssue({
                            code: z.ZodIssueCode.custom,
                            path: [],
                            message: "Số lượng item tối đa cho 1 order là 100",
                        });
                    }
                })
        })
    });

