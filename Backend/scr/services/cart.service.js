import { findManyCart, findCart, createCart, updateCart, deleteCart, deleteManyCart } from "../repositories/cart.repository.js";

export const findCartByUserId = async (userId) =>
    await findManyCart(
        { user_id: userId },
        {
            id: true,
            quantity: true,
            price_at_add: true,
            stock: {
                select: {
                    id: true,
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

export const createCartDetail = async (userId, stock_id, quantity, price_at_add) =>
    await createCart(
        {
            user_id: userId,
            stock_id,
            quantity,
            price_at_add
        }
    );

export const updateCartDetail = async (userId, cartId, quantity, price_at_add) =>
    await updateCart(
        {
            user_id: userId,
            id: cartId
        },
        {
            quantity,
            price_at_add
        }
    );

export const deleteCartDetail = async (userId, cartId) =>
    await deleteCart({
        user_id: userId,
        id: cartId
    });

export const deleteManyCartDetail = async (userId) =>
    await deleteManyCart({
        user_id: userId
    });

