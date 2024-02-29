package hu.otp.ticket.service.ticketapi.client;

import java.util.List;

import hu.otp.ticket.service.ticket.api.client.ApiClient;
import hu.otp.ticket.service.ticket.api.client.api.EventQueryControllerApi;
import hu.otp.ticket.service.ticket.api.client.api.ReservationControllerApi;
import hu.otp.ticket.service.ticket.api.client.model.EventDTO;
import hu.otp.ticket.service.ticket.api.client.model.EventDetailsDTO;
import hu.otp.ticket.service.ticket.api.client.model.ReservationResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Component
public class TicketApiClient {

    private final EventQueryControllerApi eventQueryControllerApi;

    private final ReservationControllerApi reservationControllerApi;

    @Autowired
    public TicketApiClient(ApiClient apiClient) {
        this.eventQueryControllerApi = new EventQueryControllerApi(apiClient);
        this.reservationControllerApi = new ReservationControllerApi(apiClient);
    }

    public List<EventDTO> getAllEvents(Long userId, String token) {
        return eventQueryControllerApi.getAllEvents(userId, token);
    }

    public EventDetailsDTO getEventDetails(Long userId, Long eventId, String token) {
        return eventQueryControllerApi.getEvent(userId, eventId, token);
    }

    public ReservationResponseDTO reserveSeat(Long eventId, String seatCode, Long userId, String cardId, String token) {
        return reservationControllerApi.reserveSeat(eventId, seatCode, userId, cardId, token);

    }
}