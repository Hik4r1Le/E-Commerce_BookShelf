import CartService from "../services/cart.service.js";

class CartController {
    async getCart(req, res, next) {
        try {
            const carts = await CartService.getCartByUserId(req.user.id);
            res.status(200).json({ success: true, data: carts });
        } catch (err) {
            next(err);
        }
    }

    async addCartDetail(req, res, next) {
        try {
            const cart = await CartService.addCartDetail(req.user.id, req.body);
            res.status(201).json({ success: true, data: cart });
        } catch (error) {
            next(error);
        }
    }

    async updateCartDetail(req, res, next) {
        try {
            const cart = await CartService.updateCartDetail(req.user.id, req.body, req.params.id);
            res.status(201).json({ success: true, data: cart });
        } catch (error) {
            next(error);
        }
    }

    async deleteCartDetail(req, res, next) {
        try {
            await CartService.deleteCartDetail(req.user.id, req.params.id);
            res.status(204).json({ success: true });
        } catch (error) {
            next(error);
        }
    }

    async clearCart(req, res, next) {
        try {
            await CartService.deleteAllCartDetail(req.user.id);
            res.status(204).json({ success: true });
        } catch (error) {
            next(error);
        }
    }
}

export default new CartController();
