import { prisma } from "../config/prisma.config.js"
import { queryBuilder } from "../utils/query.util.js"

export const findManyAddress = async (filter, option, orderBy, skip, take) =>
    await prisma.address.findMany(queryBuilder(filter, option, null, orderBy, skip, take));

export const createAddress = async (data, option) =>
    await prisma.address.create(queryBuilder(null, option, data));
