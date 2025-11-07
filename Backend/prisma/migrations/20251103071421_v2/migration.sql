-- DropForeignKey
ALTER TABLE `products` DROP FOREIGN KEY `products_seller_id_new_fkey`;

-- AlterTable
ALTER TABLE `products` ADD COLUMN `description` VARCHAR(255) NULL,
    MODIFY `author_name` VARCHAR(255) NULL;

-- AddForeignKey
ALTER TABLE `products` ADD CONSTRAINT `products_seller_id_fkey` FOREIGN KEY (`seller_id`) REFERENCES `users`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;
