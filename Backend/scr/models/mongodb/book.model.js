import mongoose from "mongoose";

const bookSchema = new mongoose.Schema(
  {
    isbn: { type: String },
    title: { type: String, required: true },
    subtitle: { type: String },
    authors: [{ type: String }],
    description: { type: String },
    cover_image: { type: String },
    publisher: { type: String },
    publish_date: { type: Date },
    category_ids: [{ type: mongoose.Schema.Types.ObjectId, ref: "Category" }],
    language: { type: String },
    pages: { type: Number },
    format: {
      type: String,
      enum: ["paperback", "hardcover", "ebook", "audiobook"],
      default: "paperback",
    },
  },
  { timestamps: true }
);

export default mongoose.model("Book", bookSchema);
