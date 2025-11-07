import app from "./scr/app.js"

const port = process.env.PORT || 3000;
const backend_url = process.env.BACKEND_URL || "http://localhost";

app.listen(port, () =>
    console.log(`Server running at ${backend_url}`)
);