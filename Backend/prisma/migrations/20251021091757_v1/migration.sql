/*
  Warnings:

  - Added the required column `updated_at` to the `addresses` table without a default value. This is not possible if the table is not empty.
  - Added the required column `updated_at` to the `cart_details` table without a default value. This is not possible if the table is not empty.
  - Made the column `created_at` on table `carts` required. This step will fail if there are existing NULL values in that column.
  - Made the column `updated_at` on table `carts` required. This step will fail if there are existing NULL values in that column.
  - Made the column `created_at` on table `coupons` required. This step will fail if there are existing NULL values in that column.
  - Made the column `updated_at` on table `coupons` required. This step will fail if there are existing NULL values in that column.
  - Added the required column `updated_at` to the `order_details` table without a default value. This is not possible if the table is not empty.
  - Made the column `created_at` on table `orders` required. This step will fail if there are existing NULL values in that column.
  - Made the column `updated_at` on table `orders` required. This step will fail if there are existing NULL values in that column.
  - Made the column `created_at` on table `stocks` required. This step will fail if there are existing NULL values in that column.
  - Made the column `updated_at` on table `stocks` required. This step will fail if there are existing NULL values in that column.
  - Added the required column `updated_at` to the `user_profiles` table without a default value. This is not possible if the table is not empty.
  - Made the column `created_at` on table `users` required. This step will fail if there are existing NULL values in that column.
  - Made the column `updated_at` on table `users` required. This step will fail if there are existing NULL values in that column.

*/
-- AlterTable
ALTER TABLE `addresses` ADD COLUMN `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    ADD COLUMN `deleted_at` TIMESTAMP(6) NULL,
    ADD COLUMN `updated_at` TIMESTAMP(6) NOT NULL;

-- AlterTable
ALTER TABLE `cart_details` ADD COLUMN `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    ADD COLUMN `deleted_at` TIMESTAMP(6) NULL,
    ADD COLUMN `updated_at` TIMESTAMP(6) NOT NULL;

-- AlterTable
ALTER TABLE `carts` MODIFY `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    MODIFY `updated_at` TIMESTAMP(6) NOT NULL;

-- AlterTable
ALTER TABLE `coupons` MODIFY `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    MODIFY `updated_at` TIMESTAMP(6) NOT NULL;

-- AlterTable
ALTER TABLE `order_details` ADD COLUMN `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    ADD COLUMN `deleted_at` TIMESTAMP(6) NULL,
    ADD COLUMN `updated_at` TIMESTAMP(6) NOT NULL;

-- AlterTable
ALTER TABLE `orders` MODIFY `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    MODIFY `updated_at` TIMESTAMP(6) NOT NULL;

-- AlterTable
ALTER TABLE `stocks` MODIFY `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    MODIFY `updated_at` TIMESTAMP(6) NOT NULL;

-- AlterTable
ALTER TABLE `user_profiles` ADD COLUMN `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    ADD COLUMN `deleted_at` TIMESTAMP(6) NULL,
    ADD COLUMN `updated_at` TIMESTAMP(6) NOT NULL;

-- AlterTable
ALTER TABLE `users` MODIFY `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    MODIFY `updated_at` TIMESTAMP(6) NOT NULL;
