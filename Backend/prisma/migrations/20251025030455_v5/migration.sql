/*
  Warnings:

  - You are about to drop the column `is_veryfied` on the `users` table. All the data in the column will be lost.
  - A unique constraint covering the columns `[google_id]` on the table `users` will be added. If there are existing duplicate values, this will fail.

*/
-- AlterTable
ALTER TABLE `refresh_tokens` MODIFY `is_revoked` BOOLEAN NOT NULL DEFAULT false,
    MODIFY `expires_at` TIMESTAMP(6) NOT NULL;

-- AlterTable
ALTER TABLE `users` DROP COLUMN `is_veryfied`,
    ADD COLUMN `google_id` VARCHAR(255) NULL,
    ADD COLUMN `is_email_veryfied` BOOLEAN NULL DEFAULT false;

-- CreateIndex
CREATE UNIQUE INDEX `users_google_id_key` ON `users`(`google_id`);
