// Import libraries
const express = require("express");
const path = require("path");
const morgan = require("morgan");
const cookieParser = require("cookie-parser");
const bodyParser = require("body-parser");
const methodOverride = require("method-override");
const cors = require("cors");
const session = require("express-session");
// const passport = require("./config/passport");

// Import database connectors
const connectMongoDb = require("./config/mongodb");
const { admin } = require("./config/firebase");

// Connect to database
connectMongoDb();

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
const route = require("./routes");
route(app);

module.exports = app;
