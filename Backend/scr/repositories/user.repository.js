import { prisma } from "../config/prisma.config.js"
import OtpModel from "../models/mongodb/otp.model.js";

class UserRepository {
    async findUser(filter, select = null) {
        return await prisma.User.findUnique({
            where: filter,
            select
        });
    }

    async createUser(userData) {
        return await prisma.User.create({
            data: userData
        });
    }

    async updateUser(filter, userData) {
        return await prisma.User.update({
            where: filter,
            data: userData
        });
    }
}

export default new UserRepository();
