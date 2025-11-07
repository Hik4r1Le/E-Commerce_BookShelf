import express from "express"
import { getCart, addCart, updateCart, deleteCart, clearCart} from "../../controllers/cart.controller.js";
import authMiddleware from "../../middlewares/auth.middleware.js"
import { validate } from "../../middlewares/validate.middleware.js"
import { createCartSchema, updateCartSchema, deleteCartSchema } from "../../validations/schemas/cart.schema.js"

const router = express.Router();

router.get("/details", authMiddleware, getCart);
router.post("/details", validate(createCartSchema), authMiddleware, addCart);
router.patch("/details/:id", validate(updateCartSchema), authMiddleware, updateCart);
router.delete("/details/:id", validate(deleteCartSchema), authMiddleware, deleteCart);
router.delete("/clear", authMiddleware, clearCart);

export default router;
