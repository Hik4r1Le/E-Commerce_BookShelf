import { prisma } from "../config/prisma.config.js"

class StockRepository {
    async findStock(filter) {
        return await prisma.Stock.findUnique({
            where: filter
        })
    }

    async findStockWithCartDetailId(cart_detail_id, user_id) {
        return await prisma.Cart.findUnique({
            where: { user_id },
            select: {
                cartItems: {
                    where: { id: { in: cart_detail_id } },
                    select: {
                        id: true,
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
}

export default new StockRepository();
