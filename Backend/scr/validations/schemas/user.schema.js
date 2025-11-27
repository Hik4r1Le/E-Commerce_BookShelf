import { z } from "zod";

// Password rules: tối thiểu 4 ký tự, có chữ hoa/chữ thường/ số / ký tự đặc biệt
const passwordRegex =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^\w\s]).{4,}$/;

/*
.regex(
    passwordRegex,
    "Mật khẩu phải có ít nhất 1 chữ hoa, 1 chữ thường, 1 số và 1 ký tự đặc biệt"
)
*/

export const registerSchema =
    z.object({
        body: z.object({
            email: z
                .string()
                .trim()
                .email("Email không hợp lệ")
                .min(1, "Email là bắt buộc")
                .transform((s) => s.toLowerCase()),
            username: z
                .string()
                .trim()
                .min(3, "Username tối thiểu 3 ký tự")
                .max(30, "Username tối đa 30 ký tự")
                .regex(
                    /^[a-zA-Z0-9._-]+$/,
                    "Username chỉ cho phép chữ, số, '.', '_' hoặc '-'"
                ),
            password: z
                .string()
                .min(4, "Mật khẩu tối thiểu 4 ký tự"),
        }).strip(),
    });

export const loginSchema =
    z.object({
        body: z.object({
            email: z
                .string()
                .trim()
                .email("Email không hợp lệ")
                .min(1, "Email là bắt buộc")
                .transform((s) => s.toLowerCase()),
            password: z.string().min(1, "Mật khẩu là bắt buộc"),
        }).strip(),
    });

export const forgotSchema =
    z.object({
        body: z.object({
            email: z
                .string()
                .trim()
                .email("Email không hợp lệ")
                .min(1, "Email là bắt buộc")
                .transform((s) => s.toLowerCase()),
        }).strip(),
    });

export const resetSchema =
    z.object({
        body: z.object({
            password: z
                .string()
                .min(4, "Mật khẩu tối thiểu 4 ký tự"),
        }).strip(),
    });
