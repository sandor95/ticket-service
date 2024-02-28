package hu.otp.ticket.service.ticket.api.common;

public interface CommonService {

    void validateToken(Long userId, String token);
}