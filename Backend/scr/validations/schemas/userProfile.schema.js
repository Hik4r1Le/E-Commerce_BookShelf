import { z } from "zod";

const idSchema = z
    .string({
        invalid_type_error: "ID phải là chuỗi",
    })
    .trim()
    .min(1, "ID không được để trống")
    .max(255, "ID không hợp lệ");

const fullnameSchema = z
    .string({
        invalid_type_error: "Họ tên phải là chuỗi",
    })
    .trim()
    .min(1, "Họ tên không được để trống")
    .max(255, "Họ tên quá dài");

const phoneSchema = z
    .string({
        invalid_type_error: "Số điện thoại phải là chuỗi",
    })
    .trim()
    .min(8, "Số điện thoại không hợp lệ")
    .max(20, "Số điện thoại không hợp lệ");

const addressTextSchema = z
    .string({
        invalid_type_error: "Trường địa chỉ phải là chuỗi",
    })
    .trim()
    .min(1, "Trường địa chỉ không được để trống")
    .max(255, "Nội dung quá dài");

const avatarUrlSchema = z
    .string({
        invalid_type_error: "Avatar URL phải là chuỗi",
    })
    .url("Avatar URL không hợp lệ")
    .max(500, "Avatar URL quá dài");

const genderSchema = z.enum(
    ["MALE", "FEMALE", "OTHERS"],
    {
        invalid_type_error: "Giới tính không hợp lệ",
    }
);

const dobSchema = z.preprocess(
    (v) => {
        if (typeof v === "string" || v instanceof Date) {
            const date = new Date(v);
            return isNaN(date.getTime()) ? undefined : date;
        }
        return v;
    },
    z.date({
        invalid_type_error: "Ngày sinh không hợp lệ",
    })
);

export const updateUserProfileSchema = z.object({
    body: z
        .object({
            fullname: fullnameSchema.optional(),
            dob: dobSchema.optional(),
            gender: genderSchema.optional(),

            avatar_url: avatarUrlSchema.optional(),
            address_id: idSchema.optional(),

            phone_number: phoneSchema.optional(),
            street: addressTextSchema.optional(),
            district: addressTextSchema.optional(),
            city: addressTextSchema.optional(),
        })
        .refine(
            (data) =>
                Object.values(data).some((value) => value !== undefined),
            {
                message: "Phải có ít nhất một trường để cập nhật",
            }
        )
        .strip(),
});
