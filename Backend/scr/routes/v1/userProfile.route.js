import express from "express"
import authMiddleware from "../../middlewares/auth.middleware.js"
import { validate } from "../../middlewares/validate.middleware.js"
import { updateUserProfileSchema } from "../../validations/schemas/userProfile.schema.js";
import { upload } from "../../middlewares/upload.middleware.js";
import { getProfileDetail, addProfileDetail } from "../../controllers/userProfile.controller.js";

const router = express.Router();

router.get("/", authMiddleware, getProfileDetail);
router.patch("/", authMiddleware, upload.single("avatar"), validate(updateUserProfileSchema), addProfileDetail);

export default router;
