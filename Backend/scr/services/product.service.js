import { findProduct, findManyProduct } from "../repositories/product.repository.js"

export const findProductById = async (productId) =>
    await findManyProduct(
        { product_id: productId },
        {
            product: {
                select: {
                    id: true,
                    name: true,
                    tag: true,
                    author_name: true,
                    description: true,
                    discount: true,
                    rating_avg: true,
                    rating_count: true,
                    sold_count: true,
                    price: true,
                    image_url: true,
                    release_date: true,
                    stock: {
                        select: {
                            id: true,
                            quantity: true,
                        }
                    },
                    seller: {
                        select: {
                            profile: {
                                select: {
                                    fullname: true,
                                    avatar_url: true
                                }
                            }
                        }
                    },
                    review: {
                        select: {
                            id: true,
                            username: true,
                            rating: true,
                            comment: true,
                            user: {
                                select: {
                                    username: true
                                }
                            }
                        },
                        orderBy: { created_at: "desc" },
                        take: 3
                    }
                }
            },
            category: {
                select: {
                    id: true,
                    name: true,
                    slug: true,
                    description: true,
                    icon: true
                }
            }
        }
    );


export const findProductForHome = async (filter, sort, skip, take) => {
    const where = {};
    const { name, tag, author_name, category } = filter || {};

    if (name || tag || author_name) {
        where.product = {
            ...(name && { name: { contains: name } }),
            ...(tag && { tag }),
            ...(author_name && { author_name: { contains: author_name } }),
        };
    }

    if (category) {
        where.category = {
            OR: [
                { name: { contains: category } },
                { slug: { contains: category } },
            ],
        };
    }

    let orderBy;
    if (sort?.field) {
        orderBy =
            sort.field === "category"
                ? { category: { slug: sort.order } }
                : { product: { [sort.field]: sort.order } };
    }

    return await findManyProduct(
        where,
        {
            product: {
                select: {
                    id: true,
                    name: true,
                    tag: true,
                    author_name: true,
                    discount: true,
                    rating_avg: true,
                    sold_count: true,
                    price: true,
                    image_url: true,
                }
            },
            category: {
                select: {
                    id: true,
                    name: true,
                    slug: true,
                    description: true,
                    icon: true
                }
            }
        },
        orderBy,
        skip,
        take,
    );
}



