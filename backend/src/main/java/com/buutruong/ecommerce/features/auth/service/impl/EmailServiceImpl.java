package com.buutruong.ecommerce.features.auth.service.impl;

import com.buutruong.ecommerce.features.auth.service.EmailService;
import com.buutruong.ecommerce.features.auth.type.EmailType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Override
    public void sendConfirmationEmail(EmailType type, String to, String verifyLink) {
        try {
            MimeMessage mailMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(getVerificationEmailSubject(type));
            helper.setSentDate(new Date());
            helper.setText(getEmailContent(to, verifyLink, type), true);

            mailSender.send(mailMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendNotificationEmail(EmailType type, String to) {
        try {
            MimeMessage mailMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(getNotificationEmailSubject(type));
            helper.setSentDate(new Date());
            helper.setText(getNotificationContent(type, to), true);

            mailSender.send(mailMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getNotificationContent(EmailType type, String to) {
        return switch (type) {
            case UPDATE_EMAIL_ADDRESS -> buildNotificationEmailTemplate(to, "Your account's email address has been updated. If not you, please contact us or click on the link below to block your account");
            default -> "";
        };
    }

    @Contract(pure = true)
    private @NotNull String getVerificationEmailSubject(@NotNull EmailType type) {
        return switch (type) {
            case VERIFY_EMAIL -> "Confirm your email address";
            case RESET_PASSWORD -> "Reset your password";
            case DELETE_ACCOUNT -> "Delete your account";
            case UPDATE_EMAIL_ADDRESS -> "Update your email address";
        };
    }

    private @NotNull String getNotificationEmailSubject(@NotNull EmailType type) {
        return switch (type) {
          case UPDATE_EMAIL_ADDRESS -> "Your account's email address has been updated";
          default -> "";
        };
    }

    private @NotNull String getEmailContent(String to, String verifyLink, @NotNull EmailType type) {
        return switch (type) {
            case VERIFY_EMAIL ->
                    buildVerificationEmailTemplate(to, verifyLink, "We'll be happy if you spend a few of your precious time to confirm the email address below!", "Confirm your email");
            case RESET_PASSWORD ->
                    buildVerificationEmailTemplate(to, verifyLink, "There is someone who wants to reset your account's password. If it's not you, please do not share this link.", "Reset your password");
            case DELETE_ACCOUNT ->
                    buildVerificationEmailTemplate(to, verifyLink, "There is someone who wants to delete your account. If it's not you, please do not share this link.", "Delete your account");
            case UPDATE_EMAIL_ADDRESS ->
                    buildVerificationEmailTemplate(to, verifyLink, "There is someone who wants to update your email address. Please do not show this for anyone.", "Update your email address");
        };
    }

    @Contract(pure = true)
    private @NotNull String buildVerificationEmailTemplate(String to, String verifyLink, String message, String linkText) {
        return String.format("""
                <div>
                    <p>Hi, %s</p>
                    <p>%s</p>
                    <a href="%s">%s</a>
                    <p>The link will only be available for <strong>15 minutes</strong></p>
                </div>
                """, to, message, verifyLink, linkText);
    }

    @Contract(pure = true)
    private @NotNull String buildNotificationEmailTemplate(String to, String message) {
        String blockAccountUrl = "http://localhost:8080/api/v1/auth/" + to + "/block-request";
        return String.format("""
                    <div>
                        <p>Hi, %s</p>
                        <p>%s</p>
                        <a href="%s">Block your account</a>
                    </div>
                """, to, message, blockAccountUrl);
    }
}
