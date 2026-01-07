import { findOrderDetail, createOrderDetail, deleteManyOrderDetail } from "../services/order.service.js"

export const addOrder = async (req, res, next) => {
    try {
        const userId = req.user.id;
        const orderDataArray = req.validated.body.order;
        await createOrderDetail(userId, orderDataArray);
        res.status(201).json({ success: true })
    } catch (error) {
        next(error);
    }
}

export const getOrder = async (req, res, next) => {
    try {
        const userId = req.user.id;
        const order = await findOrderDetail(userId);
        res.status(200).json({ success: true, data: order })
    } catch (error) {
        next(error);
    }
}

export const updateOrder = async (req, res, next) => {
    try {

        res.status(200).json({ success: true });
    } catch (error) {
        next(error);
    }
}

export const deleteManyOrder = async (req, res, next) => {
    try {
        const userId = req.user.id;
        const orderIdArray = req.validated.body.order_id;
        await deleteManyOrderDetail(userId, orderIdArray);
        res.status(204).json({ success: true })
    } catch (error) {
        next(error);
    }
}

