import { prisma } from "../config/prisma.config.js"

class StockRepository {
    async findStock(filter) {
        return await prisma.Stock.findUnique({
            where: filter
        })
    }
}

export default new StockRepository();
