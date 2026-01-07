import express from "express"
import authMiddleware from "../../middlewares/auth.middleware.js"
import { validate } from "../../middlewares/validate.middleware.js"
import { upload } from "../../middlewares/upload.middleware.js";
import { createSellerProductSchema, updateSellerProductSchema, deleteSellerProductSchema, updateSellerOrderSchema } from "../../validations/schemas/sellerPanel.schema.js";
import { getSellerProduct, addSellerProduct, updateSellerProduct, deleteSellerProduct, getSellerOrder, updateSellerOrder, updateManySellerProduct } from "../../controllers/sellerPanel.controller.js"

const router = express.Router();

router.get("/order", authMiddleware, getSellerOrder);
router.patch("/order/:id", authMiddleware, validate(updateSellerOrderSchema), updateSellerOrder);

router.get("/", authMiddleware, getSellerProduct);
router.post("/", authMiddleware, upload.single("image"), validate(createSellerProductSchema), addSellerProduct);
router.patch("/:id", authMiddleware, upload.single("image"), validate(updateSellerProductSchema), updateSellerProduct);
router.delete("/:id", authMiddleware, validate(deleteSellerProductSchema), deleteSellerProduct);

router.patch("/", authMiddleware, updateManySellerProduct);

export default router;
