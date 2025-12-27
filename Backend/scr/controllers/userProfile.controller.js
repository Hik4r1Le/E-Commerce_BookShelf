import { findProfileDetail, updateProfileDetail } from "../services/userProfile.service.js";
import fs from "fs";

export const getProfileDetail = async (req, res, next) => {
    try {
        const userId = req.user.id;
        const profileDetail = await findProfileDetail(userId);
        res.status(200).json({ success: true, data: profileDetail });
    } catch (error) {
        next(error);
    }
}

export const addProfileDetail = async (req, res, next) => {
    try {
        const file = req.file;
        const userId = req.user.id;
        const profileData = req.validated.body;
        await updateProfileDetail(file, userId, profileData);
        if (req.file?.path) {
            fs.unlink(req.file.path, () => { });
        }
        res.status(201).json({ success: true })
    } catch (error) {
        if (req.file?.path) {
            fs.unlink(req.file.path, () => { });
        }
        next(error);
    }
}
