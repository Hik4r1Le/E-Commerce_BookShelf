import bcrypt from "bcryptjs"
import jwt from "jsonwebtoken"

export const generateAccessToken = (user, expires = null) => {
    const { id, role, email } = user;
    const payload = {};
    if (id) payload.id = id;
    if (role) payload.role = role;
    if (email) payload.email = email;

    const expiresIn = expires || process.env.JWT_EXPIRES_IN;

    return jwt.sign(
        payload,
        process.env.JWT_SECRET,
        { expiresIn, algorithm: "HS256" }
    );
}

export const hashStringByBcrypt = async (string) =>
    await bcrypt.hash(string, 10);


export const compareByBcrypt = async (string, hashString) =>
    await bcrypt.compare(string, hashString);


