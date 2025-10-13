const mongoose = require('mongoose');

const bookSchema = new mongoose.Schema({
    title: String,
    author: String,
    description: String
}, { collection: "books" });

const Book = mongoose.model("Book", bookSchema);
module.exports = Book;
