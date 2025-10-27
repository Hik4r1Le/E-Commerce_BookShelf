import express from "express"
import ProductController  from "../../controllers/product.controller.js"

const router = express.Router();

router.get("/", ProductController.getProduct);

export default router;
