import { prisma } from "../config/prisma.config.js"
import { queryBuilder } from "../utils/query.util.js";

export const findManyReview = async (filter, option, orderBy, skip, take) =>
    await prisma.review.findMany(queryBuilder(filter, option, null, orderBy, skip, take));

export const createReview = async (data, option) =>
    await prisma.review.create(queryBuilder(null, option, data));

export const updateReview = async (filter, data) =>
    await prisma.review.update(queryBuilder(filter, null, data));

export const deleteReview = async (filter) => 
    await prisma.review.delete(queryBuilder(filter));

