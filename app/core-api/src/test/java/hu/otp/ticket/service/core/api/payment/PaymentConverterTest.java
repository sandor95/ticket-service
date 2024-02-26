package hu.otp.ticket.service.core.api.payment;

import static hu.otp.ticket.service.core.api.payment.exception.PaymentError.USER_CARD_RELATION_MISMATCH;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import hu.otp.ticket.service.core.api.model.BankCard;
import hu.otp.ticket.service.core.api.model.Payment;
import hu.otp.ticket.service.core.api.model.User;
import hu.otp.ticket.service.core.api.payment.exception.PaymentException;
import hu.otp.ticket.service.core.api.payment.model.PaymentRequestDTO;
import hu.otp.ticket.service.core.api.payment.model.PaymentResponseDTO;
import hu.otp.ticket.service.core.api.persistentlayer.BankCardGateway;
import hu.otp.ticket.service.core.api.persistentlayer.UserGateway;
import hu.otp.ticket.service.model.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentConverterTest {

    static final String PAYMENT_TRANSACTION_ID = "abc-123";
    static final int ERROR_CODE = 10105;
    static final long USER_ID = 34648345234L;
    static final String CARD_ID = "cardId12";
    static final long AMOUNT = 2000;
    static final Currency CURRENCY = Currency.HUF;
    static final BankCard bankCard = BankCard.builder().cardId(CARD_ID).build();
    static final User user = User.builder().id(USER_ID).cards(Set.of(bankCard)).build();

    @Mock
    UserGateway userGateway;

    @Mock
    BankCardGateway bankCardGateway;

    @InjectMocks
    PaymentConverter converter;


    @Test
    void shouldConvertSuccessfulPaymentEntityToDTO() {
        Payment successfulPayment = Payment.builder()
                                            .paymentTransactionId(PAYMENT_TRANSACTION_ID)
                                            .build();

        PaymentResponseDTO dto = converter.convertToDTO(successfulPayment);

        assertTrue(dto.success());
        assertEquals(PAYMENT_TRANSACTION_ID, dto.paymentTransactionId());
    }

    @Test
    void shouldConvertFailedPaymentEntityToDTO() {
        Payment successfulPayment = Payment.builder()
                                            .paymentTransactionId(PAYMENT_TRANSACTION_ID)
                                            .errorCode(ERROR_CODE)
                                            .build();

        PaymentResponseDTO dto = converter.convertToDTO(successfulPayment);

        assertFalse(dto.success());
        assertEquals(PAYMENT_TRANSACTION_ID, dto.paymentTransactionId());
        assertEquals(ERROR_CODE, dto.errorCode());
    }

    @Test
    void shouldConvertDtoToEntityWhenUserAndBankCardRelationshipValid() throws PaymentException {
        PaymentRequestDTO requestDTO = PaymentRequestDTO.builder()
                                                        .userId(USER_ID)
                                                        .cardId(CARD_ID)
                                                        .paymentTransactionId(PAYMENT_TRANSACTION_ID)
                                                        .amount(AMOUNT)
                                                        .currency(CURRENCY)
                                                        .build();
        when(userGateway.getUserById(eq(USER_ID))).thenReturn(Optional.of(user));
        when(bankCardGateway.getByCardId(eq(CARD_ID))).thenReturn(Optional.of(bankCard));

        Payment payment = converter.convertToEntity(requestDTO);

        assertEquals(USER_ID, payment.getUser().getId());
        assertEquals(CARD_ID, payment.getCard().getCardId());
        assertEquals(PAYMENT_TRANSACTION_ID, payment.getPaymentTransactionId());
        assertEquals(AMOUNT, payment.getAmount());
        assertEquals(CURRENCY, payment.getCurrency());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        PaymentRequestDTO requestDTO = PaymentRequestDTO.builder()
                                                        .userId(USER_ID)
                                                        .cardId(CARD_ID)
                                                        .paymentTransactionId(PAYMENT_TRANSACTION_ID)
                                                        .amount(AMOUNT)
                                                        .currency(CURRENCY)
                                                        .build();
        when(userGateway.getUserById(eq(USER_ID))).thenReturn(Optional.empty());

        PaymentException exception = assertThrows(PaymentException.class, () -> converter.convertToEntity(requestDTO));

        assertEquals(USER_CARD_RELATION_MISMATCH, exception.getPaymentError());
    }

    @Test
    void shouldThrowExceptionWhenCardNotFound() {
        PaymentRequestDTO requestDTO = PaymentRequestDTO.builder()
                .userId(USER_ID)
                .cardId(CARD_ID)
                .paymentTransactionId(PAYMENT_TRANSACTION_ID)
                .amount(AMOUNT)
                .currency(CURRENCY)
                .build();
        when(userGateway.getUserById(eq(USER_ID))).thenReturn(Optional.of(user));
        when(bankCardGateway.getByCardId(eq(CARD_ID))).thenReturn(Optional.empty());


        PaymentException exception = assertThrows(PaymentException.class, () -> converter.convertToEntity(requestDTO));

        assertEquals(USER_CARD_RELATION_MISMATCH, exception.getPaymentError());
    }
}