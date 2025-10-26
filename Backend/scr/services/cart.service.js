import CartRepo from "../repositories/cart.repository.js";
import StockRepo from "../repositories/stock.repository.js"
import QueryBuilder from "../utils/queryBuilder.util.js"

class CartService {
    async getCartByUserId(userId) {
        const carts = await CartRepo.findAllCartDetail(userId);
        if (!carts || carts.length === 0) return await CartRepo.createCartForUser(userId);
        return carts;
    }

    async addCartDetail(userId, cartInfo) {
        const data = QueryBuilder.buildQueryForCartDetail(cartInfo);
        const { product_id } = cartInfo;
        const stock = await StockRepo.findStock({ product_id });
        const cart = await CartRepo.findCartByUserId(userId);

        return await CartRepo.createCartDetail({
            ...data,
            cart_id: cart?.id,
            stock_id: stock?.id,
        });
    }

    async updateCartDetail(userId, cartInfo, cart_detail_id) {
        const data = QueryBuilder.buildQueryForCartDetail(cartInfo);
        return await CartRepo.updateCartDetail(userId, cart_detail_id, data);
    }

    async deleteCartDetail(userId, cart_detail_id) {
        return await CartRepo.deleteCartDetail(userId, cart_detail_id);
    }

    async deleteAllCartDetail(userId) {
        return await CartRepo.deleteAllCartDetail(userId);
    }
}

export default new CartService();
