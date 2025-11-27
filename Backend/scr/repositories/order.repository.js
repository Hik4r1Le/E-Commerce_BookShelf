import { prisma } from "../config/prisma.config.js"
import { queryBuilder } from "../utils/query.util.js"

export const findManyOrder = async (filter, option, orderBy, skip, take) => {
    return await prisma.order.findMany(queryBuilder(filter, option, null, orderBy, skip, take));
}

export const createManyOrder = async (data, option) => {
    return await prisma.order.createMany(queryBuilder(null, option, data));
}





