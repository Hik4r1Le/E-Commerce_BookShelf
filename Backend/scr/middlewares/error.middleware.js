// Middleware xử lý lỗi chung
const errorHandler = (err, req, res, next) => {
    console.error(err); // log ra server

    const statusCode = err.statusCode || 500;
    const message = err.message || "Internal Server Error";
    const errorCode = err.errorCode || "UNKNOWN_SERVER_ERROR";

    res.status(statusCode).json({
        success: false,
        message,
        errorCode
    });
};

export default errorHandler;
