import { findManyAddress } from "../repositories/address.repository.js";
import { findManyCoupon } from "../repositories/coupon.repository.js";
import { findManyShippingMethod } from "../repositories/shipping.repository.js";
import { findManyCart } from "../repositories/cart.repository.js";
import { prisma } from "../config/prisma.config.js";

export const findReviewCheckout = async (userId, cartIdArray) => {
    const address = await findManyAddress(
        {
            user_id: userId
        },
        {
            id: true,
            label: true,
            recipient_name: true,
            phone_number: true,
            street: true,
            district: true,
            city: true
        }
    );

    const coupon = await findManyCoupon(
        {
            usage_limit: { gt: prisma.coupon.fields.used_count },
            start_date: { lte: new Date() },
            end_date: { gte: new Date() },
        },
        {
            id: true,
            code: true,
            discount_type: true,
            discount_value: true,
            start_date: true,
            end_date: true
        }
    );

    const shipping = await findManyShippingMethod();

    let cart = await findManyCart(
        {
            id: { in: cartIdArray }
        },
        {
            id: true,
            quantity: true,
            stock: {
                select: {
                    id: true,
                    quantity: true,
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

    if (cart && cart.length > 0) {
        cart = cart.map(item => ({
            ...item,
            is_in_stock: item.quantity <= item.stock.quantity
        }));
    }

    return { address, coupon, shipping, cart };
}

