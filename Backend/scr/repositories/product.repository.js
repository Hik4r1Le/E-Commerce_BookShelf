import { prisma } from "../config/prisma.config.js"
import { queryBuilder } from "../utils/query.util.js";

export const findManyProduct = async (filter, option, orderBy, skip, take) =>
    await prisma.productCategory.findMany(queryBuilder(filter, option, null, orderBy, skip, take));

export const findProduct = async (filter, option) =>
    await prisma.productCategory.findUnique(queryBuilder(filter, option));

export const countProduct = async (filter) =>
    await prisma.productCategory.count(queryBuilder(filter));

