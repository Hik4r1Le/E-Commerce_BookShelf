const BookModel = require("../models/bookModel");
const firebase = require("../config/firebase");

function route(app) {
    // WebService GET
    app.get("/api/webservice/books", async (req, res) => {
        try {
            const books = await BookModel.find();
            res.status(201).json(books);
        } catch (error) {
            res.status(500).json({ message: error.message });
        }
    });
    // WebService POST
    app.post("/api/webservice/books", async (req, res) => {
        const newBook = new BookModel(req.body);
        try {
            const savedBook = await newBook.save();
            res.status(201).json(savedBook);
        } catch (error) {
            res.status(400).json({ message: error.message });
        }
    });
    // Firebase GET
    app.get("/api/firebase/books", async (req, res) => {
        try {
            const ref = firebase.db.ref('books');
            const snapshot = await ref.once('value');
            const data = snapshot.val() || {};
            res.json({ success: true, data });
        } catch (err) {
            console.error(err);
            res.status(500).json({ success: false, error: err.message });
        }
    });
    // Firebase POST
    app.post('/api/firebase/books', async (req, res) => {
        try {
            const newBook = req.body;
            const ref = firebase.db.ref('books');
            const newRef = ref.push(); // tạo key ngẫu nhiên
            await newRef.set(newBook);

            res.status(201).json({ success: true, id: newRef.key });
        } catch (err) {
            console.error(err);
            res.status(500).json({ success: false, error: err.message });
        }
    });
}

module.exports = route;
