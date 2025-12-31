import { prisma } from "../config/prisma.config.js"
import { queryBuilder } from "../utils/query.util.js"

export const findUserProfile = async (filter, option) =>
    await prisma.userProfile.findUnique(queryBuilder(filter, option));

export const updateUserProfile = async (filter, option, data) =>
    await prisma.userProfile.update(queryBuilder(filter, option, data));
