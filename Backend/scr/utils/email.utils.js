import nodemailer from "nodemailer";

export const sendOtpByEmail = async (otp, toEmail) => {
    const transporter = nodemailer.createTransport({
        service: "gmail", 
        auth: {
            user: process.env.EMAIL_FROM,          
            pass: process.env.EMAIL_APP_PASSWORD,  
        },
    });

    const mailOptions = {
        from: `"E-Commerce BookShelf" <${process.env.EMAIL_FROM}>`,
        to: toEmail,
        subject: "Mã xác thực OTP",
        html: `
            <div style="font-family: Arial, sans-serif; line-height: 1.6;">
                <h2>Xác thực tài khoản của bạn</h2>
                <p>Cảm ơn bạn đã đăng ký. Vui lòng sử dụng mã OTP dưới đây để hoàn tất quá trình xác thực:</p>
                <p style="font-size: 24px; font-weight: bold; color: #333;">${otp}</p>
                <p>Mã này sẽ hết hạn trong 5 phút.</p>
                <p>Nếu bạn không yêu cầu mã này, vui lòng bỏ qua email này.</p>
                <hr/>
                <p>Trân trọng,<br/>E-Commerce BookShelf</p>
            </div>
        `,
    };

    try {
        await transporter.sendMail(mailOptions);
    } catch (error) {
        throw new Error(`Can not send email: ${error.message}`);
    }
};
