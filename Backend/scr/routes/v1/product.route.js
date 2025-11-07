import express from "express"
import { getProductDetail, getProductForHome, addProduct }  from "../../controllers/product.controller.js"
import authMiddleware from "../../middlewares/auth.middleware.js"
import { validate } from "../../middlewares/validate.middleware.js"
import { getProductSchema, getProductDetailSchema } from "../../validations/schemas/product.schema.js"

const router = express.Router();

router.get("/home", validate(getProductSchema), getProductForHome)
router.get("/:id", validate(getProductDetailSchema), getProductDetail);

export default router;
