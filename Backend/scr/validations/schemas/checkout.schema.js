import { z } from "zod";

/**
 * Regex cho MongoDB ObjectId (24 hex) và UUID v4 (cơ bản)
 */
const objectIdRegex = /^[a-fA-F0-9]{24}$/;
const uuidRegex =
    /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;

/**
 * Hàm preprocess cho cart_id:
 * - Nếu client gửi array thì giữ nguyên
 * - Nếu client gửi JSON-string (vd: '["id1","id2"]') -> parse JSON
 * - Nếu client gửi string comma-separated ('id1,id2') -> split thành array
 * - Trường hợp khác trả undefined để Zod báo lỗi
 */
const parseCartIdPreprocess = (value) => {
    if (value === undefined || value === null) return undefined;

    // Nếu đã là array thì dùng luôn
    if (Array.isArray(value)) return value;

    // Nếu là chuỗi
    if (typeof value === "string") {
        const s = value.trim();
        if (s === "") return undefined;

        // Try JSON.parse
        try {
            const parsed = JSON.parse(s);
            if (Array.isArray(parsed)) return parsed;
        } catch (e) {
            // ignore
        }

        // Fallback: comma-separated
        if (s.includes(",")) {
            return s.split(",").map((it) => it.trim()).filter(Boolean);
        }

        // Single id string -> treat as single-element array
        return [s];
    }

    // Không hợp lệ
    return undefined;
};

/**
 * Schema cho từng id trong cart_id array:
 * - Phải là chuỗi non-empty
 * - Phải match ObjectId hoặc UUID
 */

/**
 * .refine((val) => objectIdRegex.test(val) || uuidRegex.test(val), {
        message: "cart_id phải là MongoDB ObjectId (24 hex) hoặc UUID hợp lệ",
    })
 */
const cartIdItemSchema = z
    .string()
    .trim()
    .min(1, "cart_id không được rỗng");

/**
 * getReviewCheckoutSchema:
 * - body: { cart_id: ["id1", "id2", ...] }
 * - Yêu cầu: cart_id là mảng, ít nhất 1 phần tử, tối đa 100 phần tử, không trùng lặp
 * - Kết quả: strip() để loại bỏ field thừa trong body
 */
export const getReviewCheckoutSchema =
    z.object({
        body: z
            .object({
                cart_id: z
                    .preprocess(
                        parseCartIdPreprocess,
                        z.array(cartIdItemSchema)
                            .min(1, "Phải có ít nhất 1 cart_id")
                            .max(100, "Số lượng cart_id tối đa là 100")
                    ),
            })
            .superRefine((data, ctx) => {
                // Kiểm tra trùng lặp
                const { cart_id } = data;
                if (Array.isArray(cart_id)) {
                    const seen = new Set();
                    for (let i = 0; i < cart_id.length; i++) {
                        const id = cart_id[i];
                        if (seen.has(id)) {
                            ctx.addIssue({
                                code: z.ZodIssueCode.custom,
                                path: ["cart_id", i],
                                message: `cart_id "${id}" bị trùng lặp`,
                            });
                        } else {
                            seen.add(id);
                        }
                    }
                }
            })
            .strip(),
    });

