import { MailerSend, EmailParams, Sender, Recipient } from "mailersend";

const mailerSend = new MailerSend({
    apiKey: process.env.MAILERSEND_API_KEY,
});

class EmailUtil {
    async sendOtpByEmail(otp, toEmail) {
        const sentFrom = new Sender(process.env.EMAIL_FROM, "E-Commerce BookShelf");
        const recipients = [
            new Recipient(toEmail)
        ];

        const emailParams = new EmailParams()
            .setFrom(sentFrom)
            .setTo(recipients)
            .setSubject("Mã xác thực OTP")
            .setHtml(`
                <div style="font-family: Arial, sans-serif; line-height: 1.6;">
                    <h2>Xác thực tài khoản của bạn</h2>
                    <p>Cảm ơn bạn đã đăng ký. Vui lòng sử dụng mã OTP dưới đây để hoàn tất quá trình xác thực:</p>
                    <p style="font-size: 24px; font-weight: bold; color: #333;">${otp}</p>
                    <p>Mã này sẽ hết hạn trong 5 phút.</p>
                    <p>Nếu bạn không yêu cầu mã này, vui lòng bỏ qua email này.</p>
                    <hr/>
                    <p>Trân trọng,<br/>E-Commerce BookShelf</p>
                </div>
            `);

        await mailerSend.email.send(emailParams);
    };
}

export default new EmailUtil();
