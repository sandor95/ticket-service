package hu.otp.ticket.service.partner.client;

import hu.otp.ticket.service.partner.client.api.EventQueryControllerApi;
import hu.otp.ticket.service.partner.client.api.ReservationControllerApi;
import hu.otp.ticket.service.partner.client.model.EventDataDTO;
import hu.otp.ticket.service.partner.client.model.EventDetailsDataDTO;
import hu.otp.ticket.service.partner.client.model.ReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Component
public class PartnerClient {

    private final EventQueryControllerApi eventQueryApi;

    private final ReservationControllerApi reservationApi;

    @Autowired
    public PartnerClient(ApiClient apiClient) {
        this.eventQueryApi = new EventQueryControllerApi(apiClient);
        this.reservationApi = new ReservationControllerApi(apiClient);
    }

    public EventDataDTO getEvents() {
        return eventQueryApi.getEvents();
    }

    public EventDetailsDataDTO getEvent(Long eventId) {
        return eventQueryApi.getEvent(eventId);
    }

    public ReservationDTO reserveSeat(String eventId, String seatId) {
        return reservationApi.reserve(eventId, seatId);
    }
}