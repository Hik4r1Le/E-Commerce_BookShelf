class QueryBuilder {
    buildFilterForHome(queryParams) {
        const filter = {};
        const {
            id,
            name,
            category,       
            author,
            tag,
            rating_min,
            rating_max,
            isbn,
            title,
            format,
            minPrice,
            maxPrice
        } = queryParams;

        if (id) {
            filter._id = id; 
        }

        if (name) {
            filter.name = { $regex: name, $options: "i" };
        }

        if (category) {
            filter.$or = [
                { "categories._id": category },
                { "categories.name": { $regex: category, $options: "i" } }
            ];
        }

        if (author) {
            filter["book.authors"] = { $elemMatch: { $regex: author, $options: "i" } };
        }

        if (tag) {
            filter.tag = tag;
        }

        if (rating_min || rating_max) {
            filter.rating_avg = {};
            if (rating_min) filter.rating_avg.$gte = Number(rating_min);
            if (rating_max) filter.rating_avg.$lte = Number(rating_max);
        }

        if (isbn) {
            filter["book.isbn"] = isbn;
        }

        if (title) {
            filter["book.title"] = { $regex: title, $options: "i" };
        }

        if (format) {
            filter["book.format"] = format;
        }

        if (minPrice || maxPrice) {
            filter.price = {};
            if (minPrice) filter.price.$gte = Number(minPrice);
            if (maxPrice) filter.price.$lte = Number(maxPrice);
        }

        return filter;
    }


    buildSort(queryParams) {
        const { sortBy = "createdAt", order = -1 } = queryParams;
        return { [sortBy]: Number(order) };
    }

    buildPagination(queryParams) {
        const page = Number(queryParams.page) || 1;
        const limit = Number(queryParams.limit) || 10;
        return { skip: (page - 1) * limit, limit };
    }

    buildFilterForUser(queryParams) {
        const filter = {};
        const { id, email, username, password_hash, role, is_email_verified, google_id } = queryParams;

        if (id) filter.id = id;
        if (email) filter.email = email;
        if (username) filter.username = username;
        if (password_hash) filter.password_hash = password_hash;
        if (role) filter.role = role;
        if (is_email_verified) filter.is_email_verified = is_email_verified;
        if (google_id) filter.google_id = google_id;

        return filter;
    }

    buildQueryForCartDetail(query) {
        const data = {};
        const { product_name, cart_id, quantity, price_at_add, author_name, category, cover_image, stock_id } = query;
        if (product_name) data.product_name = product_name;
        if (cart_id) data.cart_id = cart_id;
        if (quantity) data.quantity = quantity;
        if (price_at_add) data.price_at_add = price_at_add;
        if (author_name) data.author_name = author_name;
        if (category) data.category = category;
        if (cover_image) data.cover_image = cover_image;
        if (stock_id) data.stock_id = stock_id;

        return data;
    }
}

export default new QueryBuilder();
