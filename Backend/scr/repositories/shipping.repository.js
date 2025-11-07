import { prisma } from "../config/prisma.config.js"
import { queryBuilder } from "../utils/query.util.js"

export const findManyShippingMethod = async (filter, option, orderBy, skip, take) =>
    await prisma.shippingMethod.findMany(queryBuilder(filter, option, null, orderBy, skip, take));

