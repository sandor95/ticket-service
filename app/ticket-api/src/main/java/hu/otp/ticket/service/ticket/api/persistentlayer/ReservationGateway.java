package hu.otp.ticket.service.ticket.api.persistentlayer;

import hu.otp.ticket.service.ticket.api.model.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class ReservationGateway {

    private final ReservationRepository repository;

    public Reservation save(Reservation reservation) {
        return repository.save(reservation);
    }
}