import { findManyOrder, createManyOrder } from "../repositories/order.repository.js";
import { createAddress } from "../repositories/address.repository.js";

export const createOrderDetail = async (userId, orderDataArray) => {
    let newAddress = null;
    if (orderDataArray?.some(item => item?.address_id === "NEW_ADDRESS")) {
        newAddress = await createAddress(
            {
                user_id: userId,
                recipient_name: orderDataArray[0].recipient_name,
                phone_number: orderDataArray[0].phone_number,
                street: orderDataArray[0].street,
                district: orderDataArray[0].district,
                city: orderDataArray[0].city,
            },
            {
                id: true,
            }
        )
    }

    return await createManyOrder(orderDataArray.map(item => ({
        ...item,
        address_id: newAddress.id ?? item.address_id,
        user_id: userId
    })));
}


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



