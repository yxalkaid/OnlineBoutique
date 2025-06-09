package com.alkaid.emailservice_re.service;

import org.springframework.beans.factory.annotation.Autowired;

import hipstershop.Demo.Empty;
import hipstershop.Demo.OrderResult;
import hipstershop.Demo.SendOrderConfirmationRequest;
import hipstershop.EmailServiceGrpc.EmailServiceImplBase;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
public class EmailService extends EmailServiceImplBase{
    
    @Autowired
    private AsyncEmailService asyncEmailService;

    @Override
    public void sendOrderConfirmation(
        SendOrderConfirmationRequest request, 
        StreamObserver<Empty> responseObserver
    ) {
        String email = request.getEmail();
        OrderResult order = request.getOrder();

        log.info("A request to send order confirmation email to {} has been received.", email);

        try {
            asyncEmailService.sendOrderConfirmation(email, order);

            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Error initiating async email sending: {}", e.getMessage(), e);
            responseObserver.onError(io.grpc.Status.INTERNAL.withDescription("Failed to initiate email sending").asRuntimeException());
        }
    }
}
