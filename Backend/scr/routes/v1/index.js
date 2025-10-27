import productRouter from "./product.route.js";
import homeRouter from "./home.route.js";
import cartRouter from "./cart.route.js";
import authRouter from "./auth.route.js"

const v1Routes = (app) => {
    // Product API
    app.use("/api/v1/products", productRouter);

    // Home API
    app.use("/api/v1/home", homeRouter);

    // Cart API
    app.use("/api/v1/cart", cartRouter);

    // Authentication API
    app.use("/api/v1/auth", authRouter);
};

export default v1Routes;
