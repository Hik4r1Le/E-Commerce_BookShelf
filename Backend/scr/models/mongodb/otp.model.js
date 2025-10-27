import mongoose from "mongoose"

const otpSchema = new mongoose.Schema(
    {
        email: { type: String, required: true },
        otp_hash: { type: String },
        type: {
            type: String,
            enum: ["verify_email", "reset_password"],
            default: "verify_email"
        }
    },
    { timestamps: true }
);

export default new mongoose.model("Otp", otpSchema);
