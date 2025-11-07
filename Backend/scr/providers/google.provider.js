import passport from "passport";
import { Strategy as GoogleStrategy } from "passport-google-oauth20";
import { loginOrRegisterWithGoogle } from "../services/auth.service.js";

passport.use(new GoogleStrategy({
    clientID: process.env.GOOGLE_CLIENT_ID,
    clientSecret: process.env.GOOGLE_CLIENT_SECRET,
    callbackURL: `${process.env.BACKEND_URL}/api/v1/auth/google/callback`,
    passReqToCallback: true
}, async (req, accessToken, refreshToken, profile, done) => {
    // Passport tự gửi request tới Google để đổi CODE sang TOKEN và PROFILE trong nội bộ của passport
    // Sau đó trả về accessToken, refreshToken, profile ở đây
    try {
        const token = await loginOrRegisterWithGoogle(profile);
        return done(null, { token });
    } catch (err) {
        return done(err, null);
    }
}));

export default passport;
