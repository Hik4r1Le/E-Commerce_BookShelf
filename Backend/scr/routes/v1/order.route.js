import express from "express";
import authMiddleware from "../../middlewares/auth.middleware.js"
import { addOrder, getOrder, updateOrder, deleteManyOrder } from "../../controllers/order.controller.js";
import { validate } from "../../middlewares/validate.middleware.js"
import { createOrderSchema, deleteManyOrderSchema } from "../../validations/schemas/order.schema.js"

const router = express.Router();

router.get("/", authMiddleware, getOrder);
router.post("/", validate(createOrderSchema), authMiddleware, addOrder);
router.patch("/:id", authMiddleware, updateOrder);
router.delete("/", authMiddleware, validate(deleteManyOrderSchema), deleteManyOrder);

export default router;
