import bookRouter from "./book.route.js";
import productRouter from "./product.route.js";

const v1Routes = (app) => {
    // Book API
    app.use("/api/v1/books", bookRouter);

    // Product API
    app.use("/api/v1/products", productRouter);
};

export default v1Routes;
