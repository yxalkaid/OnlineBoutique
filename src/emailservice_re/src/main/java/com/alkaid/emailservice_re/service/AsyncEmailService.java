package com.alkaid.emailservice_re.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AsyncEmailService {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    private String templateName="confirmation_CN";
    
    @Async("emailTaskExecutor")
    public void sendOrderConfirmation(String email, Object order) {

        // 渲染邮件
        String htmlContent = templateService.renderEmail(templateName, email, order);
        
        // 发送邮件
        this.sendEmail(email,"Order ", htmlContent);
    }


    /**
     * 发送邮件
     * @param email
     * @param subject
     * @param htmlContent
     */
    public void sendEmail(String email,String subject ,String htmlContent) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

        try {
            helper.setFrom(fromAddress);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);            
        } catch (MessagingException e) {
            throw new MailSendException("Sending email failed", e);
        }
    }
}
