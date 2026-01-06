import { findManySellerProductDetail, createSellerProductDetail, updateSellerProductDetail, deleteSellerProductDetail } from "../services/sellerPanel.service.js"
import fs from "fs"

export const getSellerProduct = async (req, res, next) => {
    try {
        const sellerId = req.user.id;
        const sellerProduct = await findManySellerProductDetail(sellerId);
        res.status(200).json({ success: true, data: sellerProduct });
    } catch (error) {
        next(error)
    }
}

export const addSellerProduct = async (req, res, next) => {
    try {
        const file = req.file;
        const sellerId = req.user.id;
        const productData = req.validated.body;
        await createSellerProductDetail(sellerId, file, productData);
        if (req.file?.path) {
            fs.unlink(req.file.path, () => { });
        }
        res.status(201).json({ success: true });
    } catch (error) {
        if (req.file?.path) {
            fs.unlink(req.file.path, () => { });
        }
        next(error);
    }
}

export const updateSellerProduct = async (req, res, next) => {
    try {
        const file = req.file;
        const sellerId = req.user.id;
        const productData = req.validated.body;
        const productId = req.validated.params.id;
        await updateSellerProductDetail(sellerId, productId, file, productData);
        if (req.file?.path) {
            fs.unlink(req.file.path, () => { });
        }
        res.status(201).json({ success: true })
    } catch (error) {
        if (req.file?.path) {
            fs.unlink(req.file.path, () => { });
        }
        next(error);
    }
}

export const deleteSellerProduct = async (req, res, next) => {
    try {
        const sellerId = req.user.id;
        const productId = req.validated.params.id;
        await deleteSellerProductDetail(productId, sellerId);
        res.status(204).json({ success: true });
    } catch (error) {
        next(error);
    }
}
