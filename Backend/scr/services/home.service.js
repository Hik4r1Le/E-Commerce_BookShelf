import ProductRepo from "../repositories/product.repository.js"
import QueryBuilder from "../utils/queryBuilder.util.js"

class HomeService {
    async getProducts(query) {
        const filter = QueryBuilder.buildFilterForHome(query);
        const sort = QueryBuilder.buildSort(query);
        const { skip, limit } = QueryBuilder.buildPagination(query);

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

            {
                $project: {
                    _id: 1,
                    book_id: 1,
                    seller_id: 1,
                    name: 1,
                    tag: 1,
                    discount: 1,
                    rating_avg: 1,
                    images: 1,
                    price: 1,
                    "categories.name": 1,
                    "categories.description": 1,
                    "categories.icon": 1
                }
            }
        ];

        return await ProductRepo.findProducts(pipeline);
    }
}

export default new HomeService();
