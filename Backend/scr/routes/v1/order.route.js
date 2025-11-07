import express from "express";
import authMiddleware from "../../middlewares/auth.middleware.js"
import { addOrder, getOrder, updateOrder } from "../../controllers/order.controller.js";
import { validate } from "../../middlewares/validate.middleware.js"
import { createOrderSchema } from "../../validations/schemas/order.schema.js"

const router = express.Router();

router.get("/", authMiddleware, getOrder);
router.post("/", validate(createOrderSchema), authMiddleware, addOrder);
router.patch("/", authMiddleware, updateOrder )

export default router;
