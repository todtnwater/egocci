package common;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class MailUtil {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";

    private static final String FROM_EMAIL = "1026wls@gmail.com";
    private static final String FROM_PASSWORD = "XXXXXXXXXXX";

    public static boolean sendMail(String to, String senderName, String senderEmail, String subject, String content) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, FROM_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(FROM_EMAIL, "Egocci Business"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setReplyTo(new jakarta.mail.Address[] {
                new InternetAddress(senderEmail, senderName)
            });

            message.setSubject("[Egocci Business] " + senderName + " (" + senderEmail + ") - " + subject);

            String body =
                    "보낸 사람 이름: " + senderName + "\n" +
                    "보낸 사람 이메일: " + senderEmail + "\n\n" +
                    "문의 내용:\n" +
                    content;

            message.setText(body);

            Transport.send(message);
            return true;

        } catch (Exception e) {
            System.out.println("메일 전송 오류: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}