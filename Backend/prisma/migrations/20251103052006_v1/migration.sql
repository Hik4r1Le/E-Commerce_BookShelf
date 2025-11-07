-- CreateTable
CREATE TABLE `banners` (
    `id` VARCHAR(255) NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `description` TEXT NOT NULL,
    `image_url` VARCHAR(255) NOT NULL,
    `link_url` VARCHAR(255) NOT NULL,
    `type` ENUM('HOMEPAGE', 'CATEGORY', 'PRODUCT', 'CUSTOM') NOT NULL DEFAULT 'HOMEPAGE',
    `active` BOOLEAN NOT NULL DEFAULT true,
    `position` INTEGER NOT NULL DEFAULT 0,
    `start_date` TIMESTAMP(6) NOT NULL,
    `end_date` TIMESTAMP(6) NOT NULL,
    `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` TIMESTAMP(6) NOT NULL,
    `deleted_at` TIMESTAMP(6) NULL,

    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `product_banner` (
    `id` VARCHAR(255) NOT NULL,
    `product_id` VARCHAR(255) NOT NULL,
    `banner_id` VARCHAR(255) NOT NULL,
    `category_id` VARCHAR(255) NOT NULL,
    `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` TIMESTAMP(6) NOT NULL,
    `deleted_at` TIMESTAMP(6) NULL,

    INDEX `product_banner_product_id_idx`(`product_id`),
    INDEX `product_banner_banner_id_idx`(`banner_id`),
    INDEX `product_banner_category_id_idx`(`category_id`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `products` (
    `id` VARCHAR(255) NOT NULL,
    `seller_id` VARCHAR(255) NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `tag` ENUM('BESTSELLER', 'HOT', 'NEW', 'DISCOUNT', 'LIMITED', 'TRENDING', 'POPULAR', 'RECOMMENDED') NOT NULL DEFAULT 'NEW',
    `author_name` VARCHAR(255) NOT NULL,
    `price` DECIMAL(10, 2) NULL,
    `discount` DECIMAL(10, 2) NULL,
    `rating_avg` DECIMAL(10, 2) NULL,
    `rating_count` INTEGER NULL,
    `sold_count` INTEGER NULL,
    `image_url` VARCHAR(255) NOT NULL,
    `release_date` TIMESTAMP(6) NULL,
    `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` TIMESTAMP(6) NOT NULL,
    `deleted_at` TIMESTAMP(6) NULL,

    INDEX `products_seller_id_idx`(`seller_id`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `categories` (
    `id` VARCHAR(255) NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `slug` VARCHAR(255) NOT NULL,
    `description` TEXT NOT NULL,
    `icon` VARCHAR(255) NOT NULL,
    `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` TIMESTAMP(6) NOT NULL,
    `deleted_at` TIMESTAMP(6) NULL,

    UNIQUE INDEX `categories_slug_key`(`slug`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `product_category` (
    `id` VARCHAR(255) NOT NULL,
    `product_id` VARCHAR(255) NOT NULL,
    `category_id` VARCHAR(255) NOT NULL,
    `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` TIMESTAMP(6) NOT NULL,
    `deleted_at` TIMESTAMP(6) NULL,

    INDEX `product_category_product_id_idx`(`product_id`),
    INDEX `product_category_category_id_idx`(`category_id`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `reviews` (
    `id` VARCHAR(255) NOT NULL,
    `product_id` VARCHAR(255) NOT NULL,
    `user_id` VARCHAR(255) NOT NULL,
    `rating` DECIMAL(10, 2) NULL,
    `comment` TEXT NOT NULL,
    `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` TIMESTAMP(6) NOT NULL,
    `deleted_at` TIMESTAMP(6) NULL,

    INDEX `reviews_product_id_idx`(`product_id`),
    INDEX `reviews_user_id_idx`(`user_id`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `users` (
    `id` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NULL,
    `username` VARCHAR(255) NULL,
    `password_hash` VARCHAR(255) NULL,
    `role` ENUM('ADMIN', 'USER', 'SELLER') NULL DEFAULT 'USER',
    `is_email_verified` BOOLEAN NULL DEFAULT false,
    `google_id` VARCHAR(255) NULL,
    `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` TIMESTAMP(6) NOT NULL,
    `deleted_at` TIMESTAMP(6) NULL,

    UNIQUE INDEX `users_email_key`(`email`),
    UNIQUE INDEX `users_google_id_key`(`google_id`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `otps` (
    `id` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NULL,
    `otp_hash` VARCHAR(255) NULL,
    `type` ENUM('VERIFY_EMAIL', 'RESET_PASSWORD') NOT NULL DEFAULT 'VERIFY_EMAIL',
    `expires_at` TIMESTAMP(6) NOT NULL,
    `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` TIMESTAMP(6) NOT NULL,
    `deleted_at` TIMESTAMP(6) NULL,

    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `refresh_tokens` (
    `id` VARCHAR(255) NOT NULL,
    `user_id` VARCHAR(255) NOT NULL,
    `token_hash` VARCHAR(255) NOT NULL,
    `is_revoked` BOOLEAN NOT NULL DEFAULT false,
    `expires_at` TIMESTAMP(6) NOT NULL,
    `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` TIMESTAMP(6) NOT NULL,
    `deleted_at` TIMESTAMP(6) NULL,

    UNIQUE INDEX `refresh_tokens_user_id_key`(`user_id`),
    INDEX `refresh_tokens_user_id_idx`(`user_id`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `user_profiles` (
    `id` VARCHAR(255) NOT NULL,
    `user_id` VARCHAR(255) NOT NULL,
    `fullname` VARCHAR(255) NULL,
    `dob` DATE NULL,
    `gender` ENUM('MALE', 'FEMALE', 'OTHERS') NULL DEFAULT 'MALE',
    `avatar_url` VARCHAR(255) NULL,
    `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` TIMESTAMP(6) NOT NULL,
    `deleted_at` TIMESTAMP(6) NULL,

    UNIQUE INDEX `user_profiles_user_id_key`(`user_id`),
    INDEX `user_profiles_user_id_idx`(`user_id`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `addresses` (
    `id` VARCHAR(255) NOT NULL,
    `user_id` VARCHAR(255) NULL,
    `label` ENUM('HOME', 'OFFICE') NULL DEFAULT 'HOME',
    `recipient_name` VARCHAR(255) NULL,
    `phone_number` VARCHAR(255) NULL,
    `street` VARCHAR(255) NULL,
    `district` VARCHAR(255) NULL,
    `city` VARCHAR(255) NULL,
    `is_default` BOOLEAN NULL,
    `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` TIMESTAMP(6) NOT NULL,
    `deleted_at` TIMESTAMP(6) NULL,

    INDEX `addresses_user_id_idx`(`user_id`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `stocks` (
    `id` VARCHAR(255) NOT NULL,
    `product_id` VARCHAR(255) NULL,
    `quantity` INTEGER NULL,
    `status` ENUM('IN_STOCK', 'OUT_OF_STOCK') NULL DEFAULT 'IN_STOCK',
    `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` TIMESTAMP(6) NOT NULL,
    `deleted_at` TIMESTAMP(6) NULL,

    UNIQUE INDEX `stocks_product_id_key`(`product_id`),
    INDEX `stocks_product_id_idx`(`product_id`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `carts` (
    `id` VARCHAR(255) NOT NULL,
    `user_id` VARCHAR(255) NULL,
    `stock_id` VARCHAR(255) NULL,
    `quantity` INTEGER NULL,
    `price_at_add` DECIMAL(10, 2) NULL,
    `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` TIMESTAMP(6) NOT NULL,
    `deleted_at` TIMESTAMP(6) NULL,

    INDEX `carts_user_id_idx`(`user_id`),
    INDEX `carts_stock_id_idx`(`stock_id`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `orders` (
    `id` VARCHAR(255) NOT NULL,
    `user_id` VARCHAR(255) NULL,
    `address_id` VARCHAR(255) NULL,
    `coupon_id` VARCHAR(255) NULL,
    `stock_id` VARCHAR(255) NULL,
    `shipping_method_id` VARCHAR(255) NULL,
    `quantity` INTEGER NULL,
    `total_price` DECIMAL(10, 2) NULL,
    `status` ENUM('PENDING', 'CONFIRMED', 'PROCESSING', 'READY_FOR_PICKUP', 'SHIPPING', 'DELIVERED', 'CANCELLED', 'RETURN_REQUESTED', 'RETURNED', 'REFUNDED') NULL DEFAULT 'PENDING',
    `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` TIMESTAMP(6) NOT NULL,
    `deleted_at` TIMESTAMP(6) NULL,

    INDEX `orders_user_id_idx`(`user_id`),
    INDEX `orders_address_id_idx`(`address_id`),
    INDEX `orders_coupon_id_idx`(`coupon_id`),
    INDEX `orders_stock_id_idx`(`stock_id`),
    INDEX `orders_shipping_method_id_idx`(`shipping_method_id`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `shipping_methods` (
    `id` VARCHAR(255) NOT NULL,
    `type` VARCHAR(255) NULL,
    `shipping_fee` DECIMAL(10, 2) NULL,
    `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` TIMESTAMP(6) NOT NULL,
    `deleted_at` TIMESTAMP(6) NULL,

    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `coupons` (
    `id` VARCHAR(255) NOT NULL,
    `code` VARCHAR(255) NULL,
    `discount_type` ENUM('FIXED', 'PERCENTAGE') NULL DEFAULT 'PERCENTAGE',
    `discount_value` DECIMAL(10, 2) NULL,
    `usage_limit` INTEGER NULL,
    `used_count` INTEGER NULL,
    `start_date` DATETIME(0) NULL,
    `end_date` DATETIME(0) NULL,
    `created_at` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` TIMESTAMP(6) NOT NULL,
    `deleted_at` TIMESTAMP(6) NULL,

    UNIQUE INDEX `coupons_code_key`(`code`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- AddForeignKey
ALTER TABLE `product_banner` ADD CONSTRAINT `product_banner_product_id_fkey` FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `product_banner` ADD CONSTRAINT `product_banner_banner_id_fkey` FOREIGN KEY (`banner_id`) REFERENCES `banners`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `product_banner` ADD CONSTRAINT `product_banner_category_id_fkey` FOREIGN KEY (`category_id`) REFERENCES `categories`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `products` ADD CONSTRAINT `products_seller_id_new_fkey` FOREIGN KEY (`seller_id`) REFERENCES `users`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `product_category` ADD CONSTRAINT `product_category_product_id_fkey` FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `product_category` ADD CONSTRAINT `product_category_category_id_fkey` FOREIGN KEY (`category_id`) REFERENCES `categories`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `reviews` ADD CONSTRAINT `reviews_product_id_fkey` FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `reviews` ADD CONSTRAINT `reviews_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `refresh_tokens` ADD CONSTRAINT `refresh_tokens_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `user_profiles` ADD CONSTRAINT `user_profiles_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `addresses` ADD CONSTRAINT `addresses_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `stocks` ADD CONSTRAINT `stocks_product_id_fkey` FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `carts` ADD CONSTRAINT `carts_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `carts` ADD CONSTRAINT `carts_stock_id_fkey` FOREIGN KEY (`stock_id`) REFERENCES `stocks`(`id`) ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `orders` ADD CONSTRAINT `orders_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `orders` ADD CONSTRAINT `orders_address_id_fkey` FOREIGN KEY (`address_id`) REFERENCES `addresses`(`id`) ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `orders` ADD CONSTRAINT `orders_coupon_id_fkey` FOREIGN KEY (`coupon_id`) REFERENCES `coupons`(`id`) ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `orders` ADD CONSTRAINT `orders_stock_id_fkey` FOREIGN KEY (`stock_id`) REFERENCES `stocks`(`id`) ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `orders` ADD CONSTRAINT `orders_shipping_method_id_fkey` FOREIGN KEY (`shipping_method_id`) REFERENCES `shipping_methods`(`id`) ON DELETE SET NULL ON UPDATE CASCADE;
