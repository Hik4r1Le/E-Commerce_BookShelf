import { findManyOrder, createManyOrder } from "../repositories/order.repository.js";

export const createOrderDetail = async (userId, orderDataArray) =>
    await createManyOrder(orderDataArray.map(item => ({
        ...item,
        user_id: userId
    })));

export const findOrderDetail = async (userId) =>
    await findManyOrder(
        {
            user_id: userId
        },
        {
            status: true,
            quantity: true,
            total_price: true,
            stock: {
                select: {
                    product: {
                        select: {
                            id: true,
                            name: true,
                            author_name: true,
                            image_url: true,
                            price: true,
                            discount: true,
                            productCategory: {
                                select: {
                                    category: {
                                        select: {
                                            name: true
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    );



