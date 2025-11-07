import mongoose from "mongoose";

const reviewSchema = new mongoose.Schema(
  {
    product_id: { type: String, ref: "Product", required: true },
    user_id: { type: String, require: true },
    user_name: { type: String, required: true },
    rating: { type: Number, required: true, min: 0, max: 5 },
    comment: { type: String },
  },
  { timestamps: true }
);

export default mongoose.model("Review", reviewSchema);
