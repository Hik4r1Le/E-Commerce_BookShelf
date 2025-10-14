const admin = require('firebase-admin');
const dotenv = require('dotenv');

if (!process.env.GOOGLE_APPLICATION_CREDENTIALS) {
  throw new Error("Vui lòng set biến môi trường GOOGLE_APPLICATION_CREDENTIALS tới file key JSON");
}

if (!admin.apps.length) {
  admin.initializeApp({
    credential: admin.credential.applicationDefault(),
    databaseURL: process.env.FIREBASE_DATABASE_URL
  });
}

const db = admin.database(); 
module.exports = { admin, db };
