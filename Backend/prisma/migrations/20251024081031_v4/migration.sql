-- CreateTable
CREATE TABLE `refresh_tokens` (
    `id` VARCHAR(255) NOT NULL,
    `user_id` VARCHAR(255) NOT NULL,
    `token_hash` VARCHAR(255) NOT NULL,
    `is_revoked` BOOLEAN NOT NULL,
    `expires_at` DATETIME(3) NOT NULL,
    `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),

    UNIQUE INDEX `refresh_tokens_user_id_key`(`user_id`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- AddForeignKey
ALTER TABLE `refresh_tokens` ADD CONSTRAINT `refresh_tokens_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;
