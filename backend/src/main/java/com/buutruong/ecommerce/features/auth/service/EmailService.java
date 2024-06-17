package com.buutruong.ecommerce.features.auth.service;

import com.buutruong.ecommerce.features.auth.type.EmailType;

public interface EmailService {
    void sendConfirmationEmail(EmailType type, String to, String verifyLink);
    void sendNotificationEmail(EmailType type, String to);
}
