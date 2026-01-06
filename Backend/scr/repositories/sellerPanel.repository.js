import { prisma } from "../config/prisma.config.js"
import { queryBuilder } from "../utils/query.util.js";

export const findManySellerProduct = async (filter, option, orderBy, skip, take) =>
    await prisma.product.findMany(queryBuilder(filter, option, null, orderBy, skip, take));

export const findManyCategory = async () => {
    return await prisma.category.findMany({
        select: {
            id: true,
            name: true,
        }
    });
}

export const createSellerProduct = async (data, option) =>
    await prisma.product.create(queryBuilder(null, option, data));

export const updateSellerProduct = async (filter, data) =>
    await prisma.product.update(queryBuilder(filter, null, data));

export const deleteSellerProduct = async (productId, sellerId) => {
  return await prisma.$transaction(async (tx) => {

    // 1. Lấy stock_id của product (nếu có)
    const stock = await tx.stock.findUnique({
      where: { product_id: productId },
      select: { id: true },
    });

    if (stock) {
      const stockId = stock.id;

      // 2. Xóa Cart
      await tx.cart.deleteMany({
        where: { stock_id: stockId },
      });

      // 3. Xóa Order
      await tx.order.deleteMany({
        where: { stock_id: stockId },
      });

      // 4. Xóa Stock
      await tx.stock.delete({
        where: { id: stockId },
      });
    }

    // 5. Xóa ProductCategory
    await tx.productCategory.deleteMany({
      where: { product_id: productId },
    });

    // 6. Xóa Review
    await tx.review.deleteMany({
      where: { product_id: productId },
    });

    // 7. Xóa Product (check seller_id)
    return await tx.product.delete({
      where: {
        id: productId,
        seller_id: sellerId,
      },
    });
  });
};



