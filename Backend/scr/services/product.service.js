import ProductRepo from "../repositories/product.repository.js"
import QueryBuilder from "../utils/queryBuilder.util.js"

class ProductService {
    async getProductById(query) {
        const filter = QueryBuilder.buildFilterForHome(query);
        const sort = QueryBuilder.buildSort(query);
        const { skip, limit } = QueryBuilder.buildPagination(query);

        const productViewFields = {
            $project: {
                createdAt: 0,
                updatedAt: 0,
                "book._id": 0,
                "book.isbn": 0,
                "book.publisher": 0,
                "book.publish_date": 0,
                "book.category_ids": 0,
                "book.createdAt": 0,
                "book.updatedAt": 0,
                "book.category_object_ids": 0,
                "categories._id": 0,
                "categories.slug": 0,
                "categories.createdAt": 0,
                "categories.updatedAt": 0,
            }
        };

        return await ProductRepo.findProductDetail(productViewFields, filter, sort, skip, limit);
    }
}

export default new ProductService();
