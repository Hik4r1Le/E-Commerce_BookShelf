import BookModel from "../models/mongodb/book.model.js"

class Book {
    static async getBook (req, res, next) {
        const bookCount = parseInt(req.query.limit) || 20;

        try {
            const books = await BookModel.find().limit(bookCount);
            res.status(200).json(books);
        } catch (error) {
            next(error);
        }
    }

    static async addBook (req, res, next) {

    }
}

export default Book;
