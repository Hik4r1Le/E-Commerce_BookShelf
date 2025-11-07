-- DropForeignKey
ALTER TABLE `reviews` DROP FOREIGN KEY `reviews_user_id_fkey`;

-- AlterTable
ALTER TABLE `reviews` ADD COLUMN `user_name` VARCHAR(255) NULL,
    MODIFY `user_id` VARCHAR(255) NULL;

-- AddForeignKey
ALTER TABLE `reviews` ADD CONSTRAINT `reviews_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE SET NULL ON UPDATE CASCADE;
