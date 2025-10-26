/*
  Warnings:

  - You are about to drop the column `book_id` on the `cart_details` table. All the data in the column will be lost.
  - You are about to drop the column `book_id` on the `order_details` table. All the data in the column will be lost.

*/
-- AlterTable
ALTER TABLE `cart_details` DROP COLUMN `book_id`;

-- AlterTable
ALTER TABLE `order_details` DROP COLUMN `book_id`;
