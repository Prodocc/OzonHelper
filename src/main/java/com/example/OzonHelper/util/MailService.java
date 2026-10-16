package com.example.OzonHelper.util;

import com.example.OzonHelper.config.MailProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MailService {
    @Value("${spring.mail.username}")
    private String username;

    private final JavaMailSender mailSender;
    private final Map<String, String> recipients;

    public MailService(
            JavaMailSender mailSender,
            MailProperties mailProperties) {
        this.mailSender = mailSender;
        this.recipients = mailProperties.getRecipients();
    }

    public void send(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setTo(text);

        mailSender.send(message);
    }

    public void sendWithAttachments(List<String> recipientNames, String subject, String text, List<Path> attachments) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            helper.setFrom(username);
            helper.setTo(recipientNames.stream().map(recipients::get).toList().toArray(String[]::new));
            helper.setSubject(subject);
            helper.setText(text);

            for (Path attachment : attachments) {
                helper.addAttachment(
                        attachment.getFileName().toString(),
                        attachment.toFile()
                );
            }

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
