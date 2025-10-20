class QueryBuilder {
    buildFilterForHome(queryParams) {
        const filter = {};
        const {
            name,
            category,        // string categoryId hoặc tên category
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

        // Filter theo tên product
        if (name) {
            filter.name = { $regex: name, $options: "i" };
        }

        // Filter theo category
        if (category) {
            // lọc theo id hoặc tên category đều ok
            filter.$or = [
                { "categories._id": category },
                { "categories.name": { $regex: category, $options: "i" } }
            ];
        }

        // Filter theo tác giả trong book
        if (author) {
            filter["book.authors"] = { $elemMatch: { $regex: author, $options: "i" } };
        }

        // Filter theo tag
        if (tag) {
            filter.tag = tag;
        }

        // Filter theo rating_avg
        if (rating_min || rating_max) {
            filter.rating_avg = {};
            if (rating_min) filter.rating_avg.$gte = Number(rating_min);
            if (rating_max) filter.rating_avg.$lte = Number(rating_max);
        }

        // Filter theo isbn (trong book)
        if (isbn) {
            filter["book.isbn"] = isbn;
        }

        // Filter theo title của book
        if (title) {
            filter["book.title"] = { $regex: title, $options: "i" };
        }

        // Filter theo format sách (ebook, paperback,...)
        if (format) {
            filter["book.format"] = format;
        }

        // Filter theo khoảng giá
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
}

export default new QueryBuilder();
