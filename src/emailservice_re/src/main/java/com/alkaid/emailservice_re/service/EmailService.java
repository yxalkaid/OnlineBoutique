package com.alkaid.emailservice_re.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import hipstershop.Demo.Empty;
import hipstershop.Demo.OrderResult;
import hipstershop.Demo.SendOrderConfirmationRequest;
import hipstershop.EmailServiceGrpc.EmailServiceImplBase;
import io.grpc.stub.StreamObserver;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
public class EmailService extends EmailServiceImplBase {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    private String templateName="confirmation_CN";

    /**
     * 发送订单确认邮件
     */
    @Override
    public void sendOrderConfirmation(SendOrderConfirmationRequest request, StreamObserver<Empty> responseObserver) {

        String email = request.getEmail();
        String subject="Order Confirmation";
        OrderResult order = request.getOrder();

        try {
            String htmlContent = templateService.renderEmail(templateName, email, order);
            
            if (htmlContent == null || htmlContent.isEmpty()) {
                log.error("Email content is missing or empty in sendOrderConfirmation");
                throw new IllegalArgumentException("Email content is required");
            }
            
            // 发送邮件
            sendEmail(email,subject, htmlContent);

            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Failed to send email: {}", e.getMessage(), e);
            responseObserver.onError(io.grpc.Status.INTERNAL.withDescription("Failed to send email").asRuntimeException());
        }
    }

    // 发送邮件核心方法
    public void sendEmail(String email,String subject ,String htmlContent) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

        try {
            helper.setFrom(fromAddress);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);

            log.info("Confirmation email sent to: {}", email);
            
        } catch (MessagingException e) {
            throw new MailSendException("Failed to create email message", e);
        }
    }
}
