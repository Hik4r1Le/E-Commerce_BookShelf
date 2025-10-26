import ProductService from "../services/product.service.js"

class ProductController {
    async getProductById(req, res, next) {
        try {
            const products = await ProductService.getProductById(req.params);
            res.status(200).json({ success: true, data: products });
        } catch (err) {
            next(err);
        }
    }

    async addProduct(req, res, next) {

    }
}

export default new ProductController();
