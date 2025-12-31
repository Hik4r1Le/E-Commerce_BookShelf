import { findReviewCheckout } from "../services/checkout.service.js";

export const getReviewCheckout = async (req, res, next) => {
    try {
        const userId = req.user.id;
        const cartIdArray = req.validated.body.cart_id;
        const review = await findReviewCheckout(userId, cartIdArray);
        res.status(200).json({ success: true, data: review });
    } catch (error) {
        next(error);
    }
}


