import { ZodError } from "zod";

export const validate = (schema) => (req, res, next) => {
    try {
        // parse dữ liệu: body, query, params
        const data = {
            body: req.body,
            query: req.query,
            params: req.params,
        };
        const parsedData = schema.safeParse(data);

        if (!parsedData.success) throw parsedData.error;

        req.validated = parsedData.data;
        next();
    } catch (error) {
        if (error instanceof ZodError) {
            return res.status(400).json({
                status: "error",
                message: "Validation failed",
                errors: error.issues.map((err) => ({
                    path: err.path.join("."),
                    message: err.message,
                })),
            });
        }
        next(error);
    }
};
