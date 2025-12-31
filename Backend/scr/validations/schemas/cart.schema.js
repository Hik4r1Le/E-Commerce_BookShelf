import { z } from "zod";

// Regex đơn giản cho MongoDB ObjectId (24 hex)
const objectIdRegex = /^[a-fA-F0-9]{24}$/;
// Regex cho UUID v4 (cơ bản)
const uuidRegex =
	/^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;

/**
 * .refine((val) => objectIdRegex.test(val) || uuidRegex.test(val), {
				message: "stock_id phải là UUID hợp lệ hoặc MongoDB ObjectId (24 hex)",
		})
 */

const idSchema = z
	.string()
	.trim()
	.min(1, "id là bắt buộc");

/**
 * quantitySchema: coerce từ string => number, phải là integer >= 1
 * - Giới hạn tối đa đặt là 1000 (tùy chỉnh nếu cần)
 */
const quantitySchema = z
	.preprocess((v) => {
		// Nếu client gửi string, chuyển thành number
		if (typeof v === "string" && v.trim() !== "") return Number(v);
		return v;
	}, z.number().int("quantity").min(1, "quantity phải >= 1").max(1000, "quantity quá lớn"));

/**
 * priceAtAddSchema: coerce sang number, >= 0
 */
const priceAtAddSchema = z.preprocess((v) => {
	if (typeof v === "string" && v.trim() !== "") return Number(v);
	return v;
}, z.number().nonnegative("price_at_add phải >= 0"));

export const createCartSchema =
	z.object({
		body: z
			.object({
				stock_id: idSchema,
				quantity: quantitySchema,
				price_at_add: priceAtAddSchema,
			})
			.strip(),
	});

export const updateCartSchema =
	z.object({
		params: z
			.object({
				id: idSchema,
			})
			.strict(),

		body: z
			.object({
				quantity: quantitySchema,
				price_at_add: priceAtAddSchema,
			})
			.strip(),
	});

export const deleteCartSchema =
	z.object({
		params: z
			.object({
				id: idSchema,
			})
			.strict(),
	});