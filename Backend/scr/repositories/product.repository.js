import ProductModel from "../models/mongodb/product.model.js"

class ProductRepository {
    async findProducts(customizedPipeline) {
        return await ProductModel.aggregate(customizedPipeline);
    }

}

export default new ProductRepository();
