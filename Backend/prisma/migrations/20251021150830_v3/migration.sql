/*
  Warnings:

  - You are about to drop the column `book_id` on the `stocks` table. All the data in the column will be lost.

*/
-- AlterTable
ALTER TABLE `stocks` DROP COLUMN `book_id`,
    ADD COLUMN `product_id` VARCHAR(255) NULL;
