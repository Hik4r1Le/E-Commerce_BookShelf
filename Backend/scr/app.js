// Import libraries
import express from "express"
import path from "path"
import morgan from "morgan"
import cookieParser from "cookie-parser"
import bodyParser from "body-parser"
import methodOverride from "method-override"
import cors from "cors"
import session from "express-session"
// const passport = require("./config/passport");

// Import database connectors
import connectMongoDB from "./config/mongodb.config.js"

// Connect to database
connectMongoDB();

// Initialize Express
const app = express();

// Global middleware
app.use(morgan("combined")); // Logging
app.use(express.json()); // Parse JSON
app.use(express.urlencoded({ extended: true })); // Parse URL-encoded
app.use(cookieParser()); // Cookie parser
app.use(bodyParser.json()); // Parser body form
app.use(methodOverride("_method")); // Override methods
// app.use(
//     cors({
//         origin: ["http://localhost:5173", "http://localhost:3000"],
//         credentials: true,
//     })
// );
// app.use(
//     session({
//         secret: process.env.SESSION_KEY,
//         resave: false,
//         saveUninitialized: true,
//         cookie: {
//             httpOnly: true,
//             secure: false,
//             sameSite: "lax",
//             maxAge: 3 * 60 * 1000,
//         },
//     })
// );
// app.use(passport.initialize());
// app.use(passport.session());


// Import & Initialize routes
import route from "./routes/index.js"
route(app);

export default app;

