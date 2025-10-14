require("dotenv").config(); // Load environment variable
const app = require("./scr/app");

const PORT = process.env.PORT || 3000;

app.listen(PORT, () =>
    console.log(`Server running at http://localhost:${PORT}`)
);