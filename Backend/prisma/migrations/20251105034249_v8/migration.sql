/*
  Warnings:

  - Added the required column `creator` to the `banners` table without a default value. This is not possible if the table is not empty.

*/
-- AlterTable
ALTER TABLE `banners` ADD COLUMN `creator` VARCHAR(255) NOT NULL;

-- CreateIndex
CREATE INDEX `banners_creator_idx` ON `banners`(`creator`);

-- CreateIndex
CREATE INDEX `coupons_provider_idx` ON `coupons`(`provider`);

-- AddForeignKey
ALTER TABLE `banners` ADD CONSTRAINT `banners_creator_fkey` FOREIGN KEY (`creator`) REFERENCES `users`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `coupons` ADD CONSTRAINT `coupons_provider_fkey` FOREIGN KEY (`provider`) REFERENCES `users`(`id`) ON DELETE SET NULL ON UPDATE CASCADE;
