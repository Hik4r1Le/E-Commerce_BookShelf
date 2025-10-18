import mongoose from "mongoose"

const connectMongoDB = async () => {
  try {
    await mongoose.connect(`${process.env.MONGODB_URI}${process.env.MONGODB_DB_NAME}`);
    console.log('MongoDB connected successfully!');
  } catch (error) {
    console.error('MongoDB connection failed:', error.message);
  }
};

export default connectMongoDB;
