package hu.otp.ticket.service.core.api.payment;

import static hu.otp.ticket.service.Const.X_USER_TOKEN;
import static hu.otp.ticket.service.core.api.CoreApiApplication.APP_NAME;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Map;

import hu.otp.ticket.service.core.api.exception.TokenException;
import hu.otp.ticket.service.core.api.model.Payment;
import hu.otp.ticket.service.core.api.payment.exception.PaymentError;
import hu.otp.ticket.service.core.api.payment.exception.PaymentException;
import hu.otp.ticket.service.core.api.payment.model.PaymentRequestDTO;
import hu.otp.ticket.service.core.api.payment.model.PaymentResponseDTO;
import hu.otp.ticket.service.core.api.tokenvalidation.validator.UserTokenValidator;
import hu.otp.ticket.service.journal.JournalService;
import hu.otp.ticket.service.journal.model.Journal;
import hu.otp.ticket.service.journal.model.JournalType;
import hu.otp.ticket.service.util.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PaymentController {

    private final UserTokenValidator tokenFormalValidator;

    private final PaymentConverter paymentConverter;

    private final PaymentService paymentService;

    private final JournalService journalService;

    @Autowired
    public PaymentController(@Qualifier("tokenFormalValidator") UserTokenValidator tokenValidator,
                             PaymentConverter paymentConverter, PaymentService paymentService,
                             JournalService journalService) {
        this.tokenFormalValidator = tokenValidator;
        this.paymentConverter = paymentConverter;
        this.paymentService = paymentService;
        this.journalService = journalService;
    }

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful payment", useReturnTypeSchema = true),
        @ApiResponse(responseCode = "400", description = "Payment failed, details in response body",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
        @ApiResponse(responseCode = "500", description = "Technical error occurred, details in response body",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @Operation(operationId = "pay", description = "Creates a seat reservation and fully handles payment transaction",
                parameters = {@Parameter(name = X_USER_TOKEN, in = ParameterIn.HEADER, description = "User token",
                            content = @Content(schema = @Schema(implementation = String.class)))}
    )
    @PostMapping("/pay")
    public PaymentResponseDTO pay(@RequestHeader Map<String, String> headers,
                                  @RequestBody PaymentRequestDTO requestDTO) throws PaymentException, TokenException {
        // TODO for ticket-api:
        //  - validate token
        //  - extract userId from token
        //  - call core-api for payment
        //  - call seat reservation
        //  - after successful reservation commit transaction (change amount, journal, etc.)
        log.info("Payment request received with ID: {}", requestDTO.paymentTransactionId());
        validateParameters(requestDTO);
        tokenFormalValidator.validate(requestDTO.userId(), headers.get(X_USER_TOKEN), null);
        Payment payment = paymentConverter.convertToEntity(requestDTO);
        Payment approvedPayment = paymentService.pay(payment);
        log.info("Successful payment: {}", approvedPayment.getPaymentTransactionId());
        PaymentResponseDTO responseDTO = paymentConverter.convertToDTO(approvedPayment);
        saveJournal(responseDTO);
        return responseDTO;
    }

    private void validateParameters(PaymentRequestDTO requestDTO) throws PaymentException {
        if (requestDTO == null || requestDTO.userId() == null
            || isBlank(requestDTO.cardId()) || isBlank(requestDTO.paymentTransactionId())) {
            throw new PaymentException(PaymentError.INVALID_PARAMETERS);
        }
    }

    private void saveJournal(PaymentResponseDTO paymentResponseDTO) {
        Journal journal = Journal.builder()
                                .application(APP_NAME)
                                .type(JournalType.PAYMENT)
                                .timestamp(Util.sysdate())
                                .content(createJournalContent(paymentResponseDTO))
                                .build();
        journalService.save(journal);
    }

    private String createJournalContent(PaymentResponseDTO response) {
        return "Successful payment transaction!\n".concat("Response: ").concat(response.toString());
    }
}