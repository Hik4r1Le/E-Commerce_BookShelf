import { findManySellerProduct, findManyCategory, findManySellerOrder, createSellerProduct, updateSellerProduct, updateSellerOrder, deleteSellerProduct, updateManySellerProduct } from "../repositories/sellerPanel.repository.js";
import cloudinary from "../config/cloudinary.config.js"
import { v4 as uuidv4 } from "uuid";

export const findManySellerProductDetail = async (sellerId) => {
    const product = await findManySellerProduct(
        {
            seller_id: sellerId
        },
        {
            id: true,
            name: true,
            author_name: true,
            description: true,
            price: true,
            image_url: true,
            stock: {
                select: {
                    quantity: true,
                    status: true,
                }
            },
            productCategory: {
                select: {
                    category: {
                        select: {
                            id: true,
                            name: true,
                        }
                    }
                }
            }
        }
    );

    const category = await findManyCategory();

    return {
        product,
        category,
    }
}

export const createSellerProductDetail = async (sellerId, image, productData = {}) => {
    let productImageUrl = "";

    if (!image) {
        const error = new Error("Produt image is required");
        error.statusCode = 400;
        throw error;
    }

    const publicId = uuidv4();

    const uploadResult = await cloudinary.uploader.upload(image.path, {
        folder: "products",
        public_id: publicId,
        overwrite: true,
        resource_type: "image"
    });

    productImageUrl = uploadResult.secure_url;

    await createSellerProduct(
        {
            id: publicId,
            seller_id: sellerId,
            name: productData.name,
            author_name: productData.author_name,
            description: productData.description,
            price: productData.price,
            image_url: productImageUrl,
            release_date: new Date(),
            productCategory: {
                create: {
                    category_id: productData.category_id
                }
            },
            stock: {
                create: {
                    quantity: productData.quantity,
                    status: productData.quantity > 0 ? "IN_STOCK" : "OUT_OF_STOCK",
                }
            }
        }
    )
}

export const updateSellerProductDetail = async (sellerId, productId, image, productData) => {
    let productImageUrl = "";

    if (image) {
        const uploadResult = await cloudinary.uploader.upload(image.path, {
            folder: "products",
            public_id: productId,
            overwrite: true,
            resource_type: "image"
        });

        productImageUrl = uploadResult.secure_url;
    }

    await updateSellerProduct(
        {
            seller_id: sellerId,
            id: productId,
        },
        {
            ...(productData.name && { name: productData.name }),
            ...(productData.author_name && { author_name: productData.author_name }),
            ...(productData.description && { description: productData.description }),
            ...(productData.price && { price: productData.price }),
            ...(productImageUrl !== "" && { image_url: productImageUrl }),
            stock: {
                update: {
                    ...(productData.quantity !== undefined && {
                        quantity: productData.quantity,
                        status: productData.quantity > 0 ? "IN_STOCK" : "OUT_OF_STOCK",
                    })
                }
            },
            ...(productData.category_id && {
                productCategory: {
                    updateMany: {
                        where: {
                            product_id: productId,
                        },
                        data: {
                            category_id: productData.category_id,
                        },
                    },
                },
            })
        }
    )
}

export const updateManySellerProductDetail = async (sellerId) => {
    await updateManySellerProduct(null, {
        seller_id: sellerId
    });
}

export const deleteSellerProductDetail = async (productId, sellerId) => {
    await deleteSellerProduct(productId, sellerId);
}

export const findManySellerOrderDetail = async (sellerId) => {
    return await findManySellerOrder(
        {
            stock: {
                product: {
                    seller_id: sellerId
                }
            }
        },
        {
            id: true,
            quantity: true,
            total_price: true,
            status: true,
            updated_at: true,
            address: {
                select: {
                    recipient_name: true,
                    street: true,
                    district: true,
                    city: true,
                }
            },
            stock: {
                select: {
                    product: {
                        select: {
                            name: true,
                            price: true,
                            image_url: true,
                        }
                    }
                }
            }
        }
    )
}

export const updateSellerOrderDetail = async (sellerId, orderId, newStatus) => {
    await updateSellerOrder(
        {
            stock: {
                product: {
                    seller_id: sellerId
                }
            },
            id: orderId
        },
        {
            status: newStatus
        }
    )
}

