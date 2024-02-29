package hu.otp.ticket.service.ticket.api.reservation;

import hu.otp.ticket.service.ticket.api.model.Reservation;
import hu.otp.ticket.service.ticket.api.reservation.exception.ReservationException;
import hu.otp.ticket.service.ticket.api.reservation.model.ReservationDTO;

public interface ReservationService {

    void checkSeatAvailable(ReservationDTO reservationDTO) throws ReservationException;

    Reservation reserve(ReservationDTO reservationDTO) throws ReservationException;

}