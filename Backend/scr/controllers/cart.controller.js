import { findCartByUserId, createCartDetail, updateCartDetail, deleteCartDetail, deleteManyCartDetail } from "../services/cart.service.js";

export const getCart = async (req, res, next) => {
    try {
        const userId = req.user.id;
        const carts = await findCartByUserId(userId);
        res.status(200).json({ success: true, data: carts });
    } catch (err) {
        next(err);
    }
}

export const addCart = async (req, res, next) => {
    try {
        const userId = req.user.id;
        const { stock_id, quantity, price_at_add } = req.validated.body;
        await createCartDetail(userId, stock_id, quantity, price_at_add);
        res.status(201).json({ success: true });
    } catch (error) {
        next(error);
    }
}

export const updateCart = async (req, res, next) => {
    try {
        const userId = req.user.id;
        const cartId = req.validated.params.id;
        const { quantity, price_at_add } = req.validated.body;
        await updateCartDetail(userId, cartId, quantity, price_at_add);
        res.status(201).json({ success: true });
    } catch (error) {
        next(error);
    }
}

export const deleteCart = async (req, res, next) => {
    try {
        const userId = req.user.id;
        const cartId = req.validated.params.id;
        await deleteCartDetail(userId, cartId);
        res.status(204).json({ success: true });
    } catch (error) {
        next(error);
    }
}

export const clearCart = async (req, res, next) => {
    try {
        const userId = req.user.id;
        await deleteManyCartDetail(userId);
        res.status(204).json({ success: true });
    } catch (error) {
        next(error);
    }
}

