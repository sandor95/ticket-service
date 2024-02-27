package hu.otp.ticket.service.core.api.exception;

import static hu.otp.ticket.service.core.api.CoreApiApplication.APP_NAME;

import hu.otp.ticket.service.core.api.payment.exception.PaymentException;
import hu.otp.ticket.service.core.api.payment.model.PaymentResponseDTO;
import hu.otp.ticket.service.exception.BaseExceptionHandler;
import hu.otp.ticket.service.journal.JournalService;
import hu.otp.ticket.service.journal.model.Journal;
import hu.otp.ticket.service.journal.model.JournalType;
import hu.otp.ticket.service.util.Util;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.Priority;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Priority(5)
@RestControllerAdvice(basePackages = "hu.otp.ticket.service.core.api")
public class CoreApiExceptionHandler extends BaseExceptionHandler {

    private final JournalService journalService;

    @ExceptionHandler(PaymentException.class)
    @ApiResponse(responseCode = "400", description = "Payment was rejected, details in response body",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponseDTO.class))})
    public ResponseEntity<PaymentResponseDTO> handleUnhandledException(PaymentException exception) {
        log.error("{} payment transaction has been rejected due {}",
                exception.getPaymentTransactionId(), exception.getPaymentError(), exception);
        PaymentResponseDTO responseDTO = PaymentResponseDTO.builder()
                                                            .errorCode(exception.getPaymentError().errorCode)
                                                            .paymentTransactionId(exception.getPaymentTransactionId())
                                                            .success(false)
                                                            .build();
        saveJournal(responseDTO, exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
    }

    private void saveJournal(PaymentResponseDTO paymentResponseDTO, PaymentException exception) {
        Journal journal = Journal.builder()
                                .application(APP_NAME)
                                .type(JournalType.PAYMENT)
                                .timestamp(Util.sysdate())
                                .content(createJournalContent(paymentResponseDTO, exception))
                                .build();
        journalService.save(journal);
    }

    private String createJournalContent(PaymentResponseDTO paymentResponseDTO, PaymentException exception) {
        return "Exception occurred during payment transaction: [".concat(exception.getPaymentTransactionId()).concat("]\n")
                .concat("Payment error: ").concat(exception.getPaymentError().name()).concat("\n")
                .concat("Response: ").concat(paymentResponseDTO.toString());
    }
}