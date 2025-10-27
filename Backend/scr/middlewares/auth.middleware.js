import jwt from 'jsonwebtoken';

const authMiddleware = (req, res, next) => {
    try {
        const authHeader = req.headers.authorization;
        if (!authHeader?.startsWith("Bearer ")) {
            return res.status(401).json({
                success: false,
                message: "Unauthorized: Token not provided",
                errorCode: "TOKEN_MISSING"
            });
        }

        const token = authHeader.split(" ")[1];
        const decoded = jwt.verify(token, process.env.JWT_SECRET); 

        // Gắn user vào request
        req.user = {
            id: decoded.id,
            role: decoded.role,
            email: decoded.email
        };

        next();
    } catch (error) {
        // Phân loại lỗi rõ ràng
        if (error.name === "TokenExpiredError") {
            return res.status(401).json({
                success: false,
                message: "Token expired",
                errorCode: "TOKEN_EXPIRED"
            });
        }
        if (error.name === "JsonWebTokenError") {
            return res.status(401).json({
                success: false,
                message: "Invalid token",
                errorCode: "TOKEN_INVALID"
            });
        }
        return res.status(500).json({
            success: false,
            message: "Internal Server Error",
            errorCode: "SERVER_ERROR"
        });
    }
};

export default authMiddleware;
