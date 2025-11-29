import { prisma } from "../config/prisma.config.js"
import { queryBuilder } from "../utils/query.util.js";

export const findUser = async (filter, option) =>
    await prisma.user.findUnique(queryBuilder(filter, option));

export const findManyUser = async (filter, option, orderBy, skip, take) =>
    await prisma.user.findMany(queryBuilder(filter, option, null, orderBy, skip, take));

export const createUser = async (data, option) =>
    await prisma.user.create(queryBuilder(null, option, data));

export const updateUser = async (filter, option, data) =>
    await prisma.user.update(queryBuilder(filter, option, data));


