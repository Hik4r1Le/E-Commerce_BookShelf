// Load environment variable
import dotenv from "dotenv";
dotenv.config();

// Import libraries
import express from "express"
import morgan from "morgan"
import cookieParser from "cookie-parser"
import bodyParser from "body-parser"
import cors from "cors"
import errorHandler from "./middlewares/error.middleware.js"
import passport from "./providers/google.provider.js"

// Import database connectors
import mongoose from "mongoose"; 
import connectMongoDB from "./config/mongodb.config.js"
import { prisma } from "./config/prisma.config.js"
import { testConnectionPrisma } from "./config/prisma.config.js"

// Connect to database
connectMongoDB();
testConnectionPrisma();

// Initialize Express
const app = express();

// Global middleware
app.use(morgan("combined")); // Logging
app.use(express.json()); // Parse JSON
app.use(express.urlencoded({ extended: true })); // Parse URL-encoded
app.use(cookieParser()); // Cookie parser
app.use(bodyParser.json()); // Parser body form
// app.use(
//     cors({
//         origin: ["http://localhost:5173", "http://localhost:3000"],
//         credentials: true,
//     })
// );

// Import & Initialize routes
import route from "./routes/index.js"
route(app);

// handle logging error and return error to client
app.use(errorHandler); 

// Graceful shutdown để đóng DB connection khi server tắt
const shutdown = async () => {
    console.log("\n🛑 Shutting down server gracefully...");

    try {
        // Close MongoDB
        await mongoose.connection.close();
        console.log("✅ MongoDB connection closed");

        // Close Prisma (MySQL)
        await prisma.$disconnect();
        console.log("✅ Prisma disconnected from MySQL");
    } catch (err) {
        console.error("❌ Error during shutdown:", err);
    } finally {
        process.exit(0);
    }
};

process.on("SIGINT", shutdown);  
process.on("SIGTERM", shutdown); 

export default app;

