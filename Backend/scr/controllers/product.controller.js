import ProductModel from "../models/mongodb/product.model.js"

class Product {
    static async getProduct(req, res, next) {
        const productCount = parseInt(req.query.limit) || 20

        try {
            const products = await ProductModel.find().limit(productCount);
            res.status(200).json(products);
        } catch (error) {
            next(error);
        }
    }

    static async addProduct(req, res, next) {

    }
}

export default Product;
