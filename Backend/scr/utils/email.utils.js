import nodemailer from "nodemailer";
import axios from "axios";

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

export const sendOtpByEmailv2 = async (otp, toEmail) => {
    try {
        await axios.post(
            "https://api.brevo.com/v3/smtp/email",
            {
                sender: {
                    name: "E-Commerce BookShelf",
                    email: process.env.BREVO_SENDER
                },
                to: [
                    {
                        email: toEmail
                    }
                ],
                subject: "Mã xác thực OTP",
                htmlContent: `
                    <div style="font-family: Arial, sans-serif;">
                        <h2>Xác thực tài khoản của bạn</h2>
                        <p>Mã OTP của bạn là:</p>
                        <h1 style="letter-spacing: 5px;">${otp}</h1>
                        <p>Mã sẽ hết hạn sau 5 phút.</p>
                    </div>
                    `
            },
            {
                headers: {
                    "api-key": process.env.BREVO_API_KEY,
                    "content-type": "application/json",
                    "accept": "application/json"
                }
            }
        );

    } catch (error) {
        let errorMessage = "Can not send email.";
        if (error.response) {
            // Lỗi từ Brevo API (có response status code)
            const status = error.response.status;
            const data = error.response.data;
            errorMessage = `Error Brevo API [${status}]: ${data.message || JSON.stringify(data)}`;
        } else if (error.request) {
            // Lỗi mạng hoặc timeout
            errorMessage = "Network error or can not connect to Brevo API.";
        } else {
            // Lỗi khác (ví dụ: lỗi trong hàm, chưa kịp gửi request)
            errorMessage = `Server error: ${error.message}`;
        }
        throw new Error(errorMessage);
    }
}
