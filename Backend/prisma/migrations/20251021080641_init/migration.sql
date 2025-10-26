-- CreateTable
CREATE TABLE `users` (
    `id` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NULL,
    `user_name` VARCHAR(255) NULL,
    `password_hash` VARCHAR(255) NULL,
    `role` ENUM('admin', 'user', 'seller') NULL DEFAULT 'user',
    `is_veryfied` BOOLEAN NULL,
    `created_at` TIMESTAMP(6) NULL,
    `updated_at` TIMESTAMP(6) NULL,
    `deleted_at` TIMESTAMP(6) NULL,

    UNIQUE INDEX `users_email_key`(`email`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `user_profiles` (
    `id` VARCHAR(255) NOT NULL,
    `user_id` VARCHAR(255) NOT NULL,
    `full_name` VARCHAR(255) NULL,
    `dob` DATE NULL,
    `gender` ENUM('male', 'female', 'others') NULL DEFAULT 'male',
    `avatar_url` VARCHAR(255) NULL,

    UNIQUE INDEX `user_profiles_user_id_key`(`user_id`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `addresses` (
    `id` VARCHAR(255) NOT NULL,
    `user_id` VARCHAR(255) NULL,
    `label` ENUM('home', 'office') NULL DEFAULT 'home',
    `recipient_name` VARCHAR(255) NULL,
    `phone_number` VARCHAR(255) NULL,
    `street` VARCHAR(255) NULL,
    `district` VARCHAR(255) NULL,
    `city` VARCHAR(255) NULL,
    `is_default` BOOLEAN NULL,

    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `stocks` (
    `id` VARCHAR(255) NOT NULL,
    `book_id` VARCHAR(255) NULL,
    `user_id` VARCHAR(255) NULL,
    `stock_quantity` INTEGER NULL,
    `stock_status` ENUM('in_stock', 'out_of_stock') NULL DEFAULT 'in_stock',
    `created_at` TIMESTAMP(6) NULL,
    `updated_at` TIMESTAMP(6) NULL,
    `deleted_at` TIMESTAMP(6) NULL,

    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `carts` (
    `id` VARCHAR(255) NOT NULL,
    `user_id` VARCHAR(255) NULL,
    `created_at` TIMESTAMP(6) NULL,
    `updated_at` TIMESTAMP(6) NULL,
    `deleted_at` TIMESTAMP(6) NULL,

    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `cart_details` (
    `id` VARCHAR(255) NOT NULL,
    `cart_id` VARCHAR(255) NULL,
    `stock_id` VARCHAR(255) NULL,
    `book_id` VARCHAR(255) NULL,
    `quantity` INTEGER NULL,
    `price_at_add` DECIMAL(10, 2) NULL,

    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `orders` (
    `id` VARCHAR(255) NOT NULL,
    `user_id` VARCHAR(255) NULL,
    `created_at` TIMESTAMP(6) NULL,
    `updated_at` TIMESTAMP(6) NULL,
    `deleted_at` TIMESTAMP(6) NULL,

    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `order_details` (
    `id` VARCHAR(255) NOT NULL,
    `order_id` VARCHAR(255) NULL,
    `book_id` VARCHAR(255) NULL,
    `address_id` VARCHAR(255) NULL,
    `coupon_id` VARCHAR(255) NULL,
    `stock_id` VARCHAR(255) NULL,
    `quantity` INTEGER NULL,
    `shipping_fee` DECIMAL(10, 2) NULL,
    `total_price` DECIMAL(10, 2) NULL,
    `status` ENUM('processing', 'refunded', 'shipped', 'delivering', 'cancelled') NULL DEFAULT 'processing',

    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `coupons` (
    `id` VARCHAR(255) NOT NULL,
    `code` VARCHAR(255) NULL,
    `discount_type` ENUM('fixed', 'percentage') NULL DEFAULT 'percentage',
    `discount_value` DECIMAL(10, 2) NULL,
    `usage_limit` INTEGER NULL,
    `used_count` INTEGER NULL,
    `start_date` DATETIME(0) NULL,
    `end_date` DATETIME(0) NULL,
    `created_at` TIMESTAMP(6) NULL,
    `updated_at` TIMESTAMP(6) NULL,
    `deleted_at` TIMESTAMP(6) NULL,

    UNIQUE INDEX `coupons_code_key`(`code`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- AddForeignKey
ALTER TABLE `user_profiles` ADD CONSTRAINT `user_profiles_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `addresses` ADD CONSTRAINT `addresses_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `stocks` ADD CONSTRAINT `stocks_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `carts` ADD CONSTRAINT `carts_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `cart_details` ADD CONSTRAINT `cart_details_cart_id_fkey` FOREIGN KEY (`cart_id`) REFERENCES `carts`(`id`) ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `cart_details` ADD CONSTRAINT `cart_details_stock_id_fkey` FOREIGN KEY (`stock_id`) REFERENCES `stocks`(`id`) ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `orders` ADD CONSTRAINT `orders_user_id_fkey` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `order_details` ADD CONSTRAINT `order_details_order_id_fkey` FOREIGN KEY (`order_id`) REFERENCES `orders`(`id`) ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `order_details` ADD CONSTRAINT `order_details_address_id_fkey` FOREIGN KEY (`address_id`) REFERENCES `addresses`(`id`) ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `order_details` ADD CONSTRAINT `order_details_coupon_id_fkey` FOREIGN KEY (`coupon_id`) REFERENCES `coupons`(`id`) ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `order_details` ADD CONSTRAINT `order_details_stock_id_fkey` FOREIGN KEY (`stock_id`) REFERENCES `stocks`(`id`) ON DELETE SET NULL ON UPDATE CASCADE;
