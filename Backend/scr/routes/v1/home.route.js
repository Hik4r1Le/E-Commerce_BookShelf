import express from "express"
import HomeController from "../../controllers/home.controller.js"
import authMiddleware from "../../middlewares/auth.middleware.js";

const router = express.Router();

router.get("/", HomeController.getHomeData);

export default router;
