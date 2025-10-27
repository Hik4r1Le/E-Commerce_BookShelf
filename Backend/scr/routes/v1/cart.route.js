import express from "express"
import CartController from "../../controllers/cart.controller.js";
import cartController from "../../controllers/cart.controller.js";
import authMiddleware from "../../middlewares/auth.middleware.js"

const router = express.Router();

router.get("/", authMiddleware, CartController.getCart);
router.post("/details", authMiddleware, CartController.addCartDetail);
router.patch("/details/:id", authMiddleware, CartController.updateCartDetail);
router.delete("/details/:id", authMiddleware, CartController.deleteCartDetail);
router.delete("/clear", authMiddleware, cartController.clearCart);

export default router;
