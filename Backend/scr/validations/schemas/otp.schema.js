import { z } from "zod";

const otpTypeEnum = z.enum(["VERIFY_EMAIL", "RESET_PASSWORD"]);
const otpRegex = /^\d{4,6}$/; // OTP regex: cho phép 4-6 chữ số

export const resendOtpSchema = 
    z.object({
        body: z.object({
            email: z
                .string()
                .trim()
                .email("Email không hợp lệ")
                .min(1, "Email là bắt buộc")
                .transform((s) => s.toLowerCase()),
            type: otpTypeEnum,
        }).strip(),
    });

export const verifyOtpSchema = 
    z.object({
        body: z.object({
            email: z
                .string()
                .trim()
                .email("Email không hợp lệ")
                .min(1, "Email là bắt buộc")
                .transform((s) => s.toLowerCase()),
            otp: z.coerce
                .string()
                .trim()
                .regex(otpRegex, "OTP không hợp lệ (phải là 4-6 chữ số)"),
            type: otpTypeEnum,
        }).strip(),
    });