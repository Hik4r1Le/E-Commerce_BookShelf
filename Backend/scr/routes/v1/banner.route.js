import express from "express"
import BannerController from "../../controllers/banner.controller.js";
import authMiddeware from "../../middlewares/auth.middleware.js"

const router = express.Router();

router.get("/", BannerController.getBanner);
router.post("/", authMiddeware, BannerController.createBanner);


export default router;
