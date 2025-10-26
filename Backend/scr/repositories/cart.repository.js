import { prisma } from "../config/prisma.config.js"

class CartRepository {
    async findAllCartDetail(userId) {
        return await prisma.Cart.findMany({
            where: {
                user_id: userId,
            },
            select: {
                id: true,
                cartItems: {
                    select: {
                        id: true,
                        quantity: true,
                        price_at_add: true,
                        product_name: true,
                        author_name: true,
                        cover_image: true,
                        category: true,
                        stock: {
                            select: {
                                id: true,
                                product_id: true,
                                stock_quantity: true
                            }
                        }
                    }
                }
            }
        });
    }

    async findCartByUserId(userId) {
        return await prisma.Cart.findUnique({
            where: {
                user_id: userId
            }
        });
    }

    async createCartDetail(cartData) {
        return await prisma.CartDetail.create({
            data: cartData
        });
    }

    async createCartForUser(userId) {
        return await prisma.Cart.create({
            data: {
                user_id: userId
            }
        });
    }

    async updateCartDetail(userId, cart_detail_id, data) {
        return await prisma.CartDetail.updateMany({
            where: {
                id: cart_detail_id,
                cart: {
                    user_id: userId
                }
            },
            data
        });
    }

    async deleteCartDetail(userId, cart_detail_id) {
        return await prisma.CartDetail.deleteMany({
            where: {
                id: cart_detail_id,
                cart: {
                    user_id: userId
                }
            }
        });
    }

    async deleteAllCartDetail(userId) {
        return await prisma.CartDetail.deleteMany({
            where: {
                cart: {
                    user_id: userId
                }
            }
        })
    }
}

export default new CartRepository();
