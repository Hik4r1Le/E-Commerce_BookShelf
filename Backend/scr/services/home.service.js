import ProductRepo from "../repositories/product.repository.js"
import UserRepo from "../repositories/user.repository.js"
import QueryBuilder from "../utils/queryBuilder.util.js"

class HomeService {
    async getProducts(request) {
        const query = request.query;
        const userId = request.user?.id || "";

        const filter = QueryBuilder.buildFilterForHome(query);
        const sort = QueryBuilder.buildSort(query);
        const { skip, limit } = QueryBuilder.buildPagination(query);

        const productViewFields = {
            $project: {
                _id: 1,
                name: 1,
                tag: 1,
                discount: 1,
                rating_avg: 1,
                images: 1,
                price: 1,
                sold_count: 1,
                "book.authors": 1,
                "categories.name": 1,
                "categories.description": 1,
                "categories.icon": 1
            }
        };

        const productData = await ProductRepo.findProductsForHome(productViewFields, filter, sort, skip, limit);
        const userData = await UserRepo.findUser({ id: userId }, { avatar: true });
        if (userData) {
            return {
                ...productData,
                avatar_user: userData.avatar
            }
        }
        return productData;
    }
}

export default new HomeService();
