import { z } from "zod";

const ALLOWED_FILTER_FIELDS = ["name", "tag", "author_name", "category"];
const ALLOWED_SORT_FIELDS = ["name", "tag", "author_name", "price", "sold_count", "rating_avg"];

/**
 * Schema con cho phần filter:
 * - Chỉ cho phép các trường cụ thể
 * - Mỗi trường là chuỗi, tự động trim, có thể rỗng (nếu client chưa nhập)
 */
const productFilterSchema = z.object({
    name: z.string().trim().min(1, "Tên không được để trống").optional(),
    tag: z.string().trim().min(1, "Tag không hợp lệ").optional(),
    author_name: z.string().trim().min(1, "Tên tác giả không hợp lệ").optional(),
    category: z.string().trim().min(1, "Danh mục không hợp lệ").optional(),
});

/**
 * Hàm parse filter — chấp nhận:
 * - JSON string: ?filter={"name":"harry","category":"fiction"}
 * - object: nếu middleware body-parser hoặc query parser đã parse sẵn
 * - undefined: nếu không gửi filter
 */
const parseFilterPreprocess = (value) => {
    if (value === undefined || value === null || value === "") return undefined;

    if (typeof value === "object") return value;

    if (typeof value === "string") {
        try {
            return JSON.parse(value);
        } catch {
            // fallback: parse "key:value,key2:value2" kiểu đơn giản
            const obj = {};
            const parts = value.split(",");
            for (const part of parts) {
                const [key, val] = part.split(":").map((s) => s.trim());
                if (ALLOWED_FILTER_FIELDS.includes(key) && val) {
                    obj[key] = val;
                }
            }
            return Object.keys(obj).length ? obj : undefined;
        }
    }
    return undefined;
};

/**
 * Hàm parse sort:
 * - Cho phép dạng "price:asc", "price:desc", "-price", "price"
 * - Chỉ cho phép field nằm trong danh sách cho phép
 */
const parseSortPreprocess = (value) => {
    if (!value || typeof value !== "string") return undefined;

    const normalized = value.trim();
    let field = normalized;
    let order = "asc";

    if (normalized.startsWith("-")) {
        field = normalized.slice(1);
        order = "desc";
    } else if (normalized.includes(":")) {
        const [f, o] = normalized.split(":");
        field = f;
        order = o.toLowerCase();
    }

    if (!ALLOWED_SORT_FIELDS.includes(field))
        throw new Error(`Trường sort '${field}' không hợp lệ`);
    if (!["asc", "desc"].includes(order))
        throw new Error(`Thứ tự sort '${order}' không hợp lệ`);

    return { field, order };
};

export const getProductSchema =
    z.object({
        query: z.object({
            filter: z
                .preprocess(parseFilterPreprocess, productFilterSchema)
                .optional(),

            sort: z
                .preprocess(parseSortPreprocess, z.object({
                    field: z.string(),
                    order: z.enum(["asc", "desc"]),
                }))
                .optional(),

            skip: z
                .preprocess(
                    (v) => (v === undefined ? 0 : Number(v)),
                    z.number().int().nonnegative().default(0)
                ),

            take: z
                .preprocess(
                    (v) => (v === undefined ? 30 : Number(v)),
                    z.number().int().positive().max(100).default(30)
                ),
        }).strip(), // không lỗi, nhưng các field ngoài những field được liệt kê sẽ bị bỏ đi
    });

const idSchema = z
    .string()
    .trim()
    .min(1, "id là bắt buộc");

export const getProductDetailSchema =
    z.object({
        params: z
            .object({
                id: idSchema,
            })
            .strict(),
    });
