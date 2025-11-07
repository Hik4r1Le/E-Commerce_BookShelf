import { prisma } from "../config/prisma.config.js"
import { queryBuilder } from "../utils/query.util.js"

export const findManyCoupon = async (filter, option, orderBy, skip, take) => {
    return await prisma.coupon.findMany(queryBuilder(filter, option, null, orderBy, skip, take));
}

export const findCoupon = async (filter, option) => {
    return await prisma.coupon.findUnique(queryBuilder(filter, option));
}

export const createCoupon = async (data, option) => {
    return await prisma.coupon.create(queryBuilder(null, option, data));
}
