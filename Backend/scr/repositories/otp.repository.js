import redis from "../config/redis.config.js";

const generateKey = (filter) => {
    const parts = Object.entries(filter).map(([key, value]) => `${key}=${value}`);
    return `otp:${parts.join(":")}`;
}

export const createOrUpdateOtp = async (filter, data) => {
    const key = generateKey(filter);
    const ttl = 60 * 5;
    await redis.set(key, JSON.stringify(data), "EX", ttl);
}

export const findOtp = async (filter) => {
    const key = generateKey(filter);
    const data = await redis.get(key);
    return data ? JSON.parse(data) : null;
}

export const checkOtpIsExpired = async (filter) => {
    const key = generateKey(filter);
    const ttl = await redis.ttl(key);

    if (ttl === -2) {
        // Key không tồn tại => OTP hết hạn hoặc chưa tạo
        return false;
    }

    if (ttl === -1) {
        // Key tồn tại nhưng không có TTL => lỗi logic hoặc thiếu set EX
        return true;
    }

    // ttl >= 0 => OTP vẫn còn hạn
    return true;
}

export const deleteOtp = async (filter) => {
    const key = generateKey(filter);
    await redis.del(key);
}



