import ProductModel from "../models/mongodb/product.model.js"
import reviewModel from "../models/mongodb/review.model.js";
import ReviewModel from "../models/mongodb/review.model.js"

class ProductRepository {
    async findProductsForHome(productViewFields, filter, sort, skip, limit) {
        const pipeline = [
            {
                $lookup: {
                    from: "books",
                    localField: "book_id",
                    foreignField: "_id",
                    as: "book"
                },
            },
            { $unwind: "$book" },

            {
                $addFields: {
                    "book.category_object_ids": {
                        $map: {
                            input: "$book.category_ids",
                            as: "catId",
                            in: { $toObjectId: "$$catId" }
                        }
                    }
                }
            },

            {
                $lookup: {
                    from: "categories",
                    localField: "book.category_object_ids",
                    foreignField: "_id",
                    as: "categories"
                }
            },

            { $match: filter },
            { $sort: sort },
            { $skip: skip },
            { $limit: limit },

            productViewFields,
        ];

        return await ProductModel.aggregate(pipeline);
    }

    async findProductDetail(productViewFields, filter, sort, skip, limit) {
        const pipeline = [
            {
                $lookup: {
                    from: "books",
                    localField: "book_id",
                    foreignField: "_id",
                    as: "book"
                },
            },
            { $unwind: "$book" },

            {
                $addFields: {
                    "book.category_object_ids": {
                        $map: {
                            input: "$book.category_ids",
                            as: "catId",
                            in: { $toObjectId: "$$catId" }
                        }
                    }
                }
            },

            {
                $lookup: {
                    from: "categories",
                    localField: "book.category_object_ids",
                    foreignField: "_id",
                    as: "categories"
                }
            },

            {
                $lookup: {
                    from: "reviews",
                    localField: "_id",
                    foreignField: "product_id",
                    as: "reviews"
                }
            },

            { $match: filter },
            { $sort: sort },
            { $skip: skip },
            { $limit: limit },

            productViewFields,
        ];

        return await ProductModel.aggregate(pipeline);
    }
}

export default new ProductRepository();
