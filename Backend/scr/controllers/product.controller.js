import { findProductById, findProductForHome } from "../services/product.service.js"

export const getProductDetail = async (req, res, next) => {
    try {
        const productId = req.validated.params.id;
        const data = await findProductById(productId);
        res.status(200).json({ success: true, data });
    } catch (err) {
        next(err);
    }
}

export const getProductForHome = async (req, res, next) => {
    try {
        const { filter, sort, skip, take } = req.validated.query;
        const data = await findProductForHome(filter, sort, skip, take);
        res.status(200).json({ success: true, data });
    } catch (err) {
        next(err);
    }
}

export const addProduct = async (req, res, next) => {

}


