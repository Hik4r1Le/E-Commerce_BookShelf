/*
  Warnings:

  - A unique constraint covering the columns `[user_id]` on the table `carts` will be added. If there are existing duplicate values, this will fail.
  - A unique constraint covering the columns `[user_id]` on the table `orders` will be added. If there are existing duplicate values, this will fail.

*/
-- CreateIndex
CREATE UNIQUE INDEX `carts_user_id_key` ON `carts`(`user_id`);

-- CreateIndex
CREATE UNIQUE INDEX `orders_user_id_key` ON `orders`(`user_id`);
