package hu.otp.ticket.service.core.api.exception;

import hu.otp.ticket.service.exception.BaseExceptionHandler;
import jakarta.annotation.Priority;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Priority(5)
@RestControllerAdvice(basePackages = "hu.otp.ticket.service.core.api")
public class CoreApiExceptionHandler extends BaseExceptionHandler {

}