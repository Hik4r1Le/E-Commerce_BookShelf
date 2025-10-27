import express from "express"
import BookController from "../../controllers/book.controller.js"

const router = express.Router();

router.get("/", BookController.getBook)

export default router;
