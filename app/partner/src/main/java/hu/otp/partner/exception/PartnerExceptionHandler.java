package hu.otp.partner.exception;

import hu.otp.ticket.service.exception.BaseExceptionHandler;
import jakarta.annotation.Priority;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Priority(5)
@RestControllerAdvice(basePackages = "hu.otp.partner")
public class PartnerExceptionHandler extends BaseExceptionHandler {

}