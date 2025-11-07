import productRouter from "./product.route.js";
import cartRouter from "./cart.route.js";
import authRouter from "./auth.route.js"
import checkoutRouter from "./checkout.route.js"
import orderRotuer from "./order.route.js"
import bannerRouter from "./banner.route.js"
import reviewRouter from "./review.route.js"

const v1Routes = (app) => {
    // Authentication API
    app.use("/api/v1/auth", authRouter);

    // Product API
    app.use("/api/v1/products", productRouter);

    // Cart API
    app.use("/api/v1/cart", cartRouter);

    // Checkout API
    app.use("/api/v1/checkout", checkoutRouter);

    // Order API
    app.use("/api/v1/order", orderRotuer);

    // Review API
    app.use("/api/v1/reviews", reviewRouter);

    // Banner API
    app.use("/api/v1/banners", bannerRouter)

};

export default v1Routes;
