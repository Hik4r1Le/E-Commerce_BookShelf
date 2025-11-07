export const queryBuilder = (filter, option, data, orderBy, skip, take) => {
    const query = {};

    if (filter && Object.keys(filter).length > 0)
        query.where = filter;

    if (option && Object.keys(option).length > 0)
        query.select = option;

    if (orderBy && Object.keys(orderBy).length > 0)
        query.orderBy = orderBy;

    if (typeof skip === "number")
        query.skip = skip;

    if (typeof take === "number")
        query.take = take;

    if (data && ((Array.isArray(data) && data.length) || Object.keys(data).length))
        query.data = data;

    return query;
};
