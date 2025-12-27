import { findUserProfile, updateUserProfile } from "../repositories/userProfile.repository.js";
import { findManyAddress, updateAddress, createAddress } from "../repositories/address.repository.js";
import cloudinary from "../config/cloudinary.config.js"
import fs from "fs";

export const findProfileDetail = async (userId) => {
    const profile = await findUserProfile(
        {
            user_id: userId,
        },
        {
            fullname: true,
            dob: true,
            gender: true,
            avatar_url: true,
        }
    );

    const address = await findManyAddress(
        {
            user_id: userId,
            is_default: true,
        },
        {
            id: true,
            recipient_name: true,
            phone_number: true,
            street: true,
            district: true,
            city: true,
        }
    )

    return {
        profile,
        address: address?.[0] ?? null,
    }
}

export const updateProfileDetail = async (image, userId, profileData = {}) => {
    let avatarUrl = profileData.avatar_url;

    if (!avatarUrl) {
        if (!image) {
            const error = new Error("Avatar image is required");
            error.statusCode = 400;
            throw error;
        }

        const uploadResult = await cloudinary.uploader.upload(image.path, {
            folder: "avatars",
            public_id: userId,
            overwrite: true,
            resource_type: "image",
            transformation: [
                {
                    width: 300,
                    height: 300,
                    crop: "pad",
                    background: "auto"
                },
                { quality: "auto" }
            ]
        });

        avatarUrl = uploadResult.secure_url;
    }

    await updateUserProfile(
        { user_id: userId },
        { id: true },
        {
            ...(profileData.fullname && { fullname: profileData.fullname }),
            ...(profileData.dob && { dob: profileData.dob }),
            ...(profileData.gender && { gender: profileData.gender }),
            ...(avatarUrl && { avatar_url: avatarUrl }),
        }
    );

    const addressData = {
        recipient_name: profileData.fullname,
        phone_number: profileData.phone_number,
        street: profileData.street,
        district: profileData.district,
        city: profileData.city,
        is_default: true,
    };

    Object.keys(addressData).forEach(
        (key) => addressData[key] === undefined && delete addressData[key]
    );

    if (profileData.address_id) {
        await updateAddress(
            {
                id: profileData.address_id,
            },
            { id: true },
            addressData
        );
    } else if (Object.keys(addressData).length > 0) {
        await createAddress({
            user_id: userId,
            ...addressData,
        });
    }
};

