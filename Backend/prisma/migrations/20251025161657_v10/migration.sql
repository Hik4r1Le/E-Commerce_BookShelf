/*
  Warnings:

  - A unique constraint covering the columns `[product_id]` on the table `stocks` will be added. If there are existing duplicate values, this will fail.

*/
-- CreateIndex
CREATE UNIQUE INDEX `stocks_product_id_key` ON `stocks`(`product_id`);
