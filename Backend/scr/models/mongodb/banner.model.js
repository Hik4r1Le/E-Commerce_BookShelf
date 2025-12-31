import mongoose from "mongoose";

const bannerSchema = new mongoose.Schema(
  {
    product_ids: [{ type: String, ref: "Product" }],
    name: { type: String, required: true },
    title: { type: String },
    description: { type: String },
    banner_image: { type: String },
    active: { type: Boolean, default: true },
    start_date: { type: Date },
    end_date: { type: Date },
  },
  { timestamps: true }
);

export default mongoose.model("Banner", bannerSchema);
