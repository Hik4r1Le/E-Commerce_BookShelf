import { prisma } from "../config/prisma.config.js"
import { queryBuilder } from "../utils/query.util.js"

export const findManyCart = async (filter, option, orderBy, skip, take) => {
    return await prisma.cart.findMany(queryBuilder(filter, option, null, orderBy, skip, take));
}

export const findCart = async (filter, option) => {
    return await prisma.cart.findUnique(queryBuilder(filter, option));
}

export const createCart = async (data, option) => {
    return await prisma.cart.create(queryBuilder(null, option, data));
}

export const updateCart = async (filter, data) => {
    return await prisma.cart.update(queryBuilder(filter, null, data));
}

export const deleteCart = async (filter) => {
    return await prisma.cart.delete(queryBuilder(filter));
}

export const deleteManyCart = async (filter) => {
    return await prisma.cart.deleteMany(queryBuilder(filter))
}

