import mongoose from "mongoose";

const productSchema = new mongoose.Schema(
  {
    product_id: { type: String, required: true, unique: true },
    book_id: { type: mongoose.Schema.Types.ObjectId, ref: "Book", required: true },
    seller_id: { type: String, required: true },
    name: { type: String, required: true },
    description: { type: String },
    tag: {
      type: String,
      enum: [
        "bestseller",
        "hot",
        "new",
        "discount",
        "limited",
        "trending",
        "popular",
        "recommended",
      ],
      default: "new",
    },
    price: { type: mongoose.Schema.Types.Decimal128, required: true },
    discount: { type: Number, default: 0, min: 0, max: 100 },
    rating_avg: { type: Number, default: 0, min: 0, max: 5 },
    rating_count: { type: Number, default: 0 },
    images: [{ type: String }],
    release_date: { type: Date },
  },
  { timestamps: true }
);

export default mongoose.model("Product", productSchema);
