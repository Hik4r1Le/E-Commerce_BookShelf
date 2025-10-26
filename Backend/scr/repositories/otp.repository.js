import OtpModel from "../models/mongodb/otp.model.js"

class OtpRepository {
    async createOrUpdateOtp(filter, otpData) {
        return await OtpModel.findOneAndUpdate(
            filter,
            { $set: otpData },
            {
                new: true,
                upsert: true,
                timestamps: true,
                setDefaultsOnInsert: true,
            }
        ).lean();
    }

    async findOtp(filter) {
        return await OtpModel.findOne(filter).lean();
    }

    async deleteOtp(id) {
        return await OtpModel.findByIdAndDelete(id).lean();
    }
}

export default new OtpRepository();
