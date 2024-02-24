package hu.otp.partner.query;

import static hu.otp.partner.knownobject.Events.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import hu.otp.partner.persistentlayer.EventPersistentLayerGateway;
import hu.otp.partner.query.model.EventDataDTO;
import hu.otp.partner.query.model.EventDetailsDataDTO;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Disabled
@Profile("test")
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventQueryControllerIT {

    @MockBean
    private EventPersistentLayerGateway gatewayMock;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void shouldReturnAllEvents() {
        when(gatewayMock.getAllEvents()).thenReturn(List.of(_30Y_CONCERT, BETON_HOFI_CONCERT));

        ResponseEntity<EventDataDTO> response = restTemplate.getForEntity("/getEvents", EventDataDTO.class);


        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        EventDataDTO eventData = response.getBody();
        assertNotNull(eventData);
        assertTrue(eventData.isSuccess());
        assertEquals(2, eventData.getData().size());
        assertTrue(eventData.getData().contains(_30Y_CONCERT_DTO));
        assertTrue(eventData.getData().contains(BETON_HOFI_CONCERT_DTO));
    }

    @Test
    void shouldReturnEventDetails() {
        when(gatewayMock.getById(eq(_30Y_CONCERT.getId()))).thenReturn(Optional.of(_30Y_CONCERT));

        ResponseEntity<EventDetailsDataDTO> response = restTemplate.getForEntity("/getEvent/" + _30Y_CONCERT.getId(), EventDetailsDataDTO.class);


        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        EventDetailsDataDTO eventDetailsData = response.getBody();
        assertNotNull(eventDetailsData);
        assertTrue(eventDetailsData.isSuccess());
        assertNotNull(eventDetailsData.getData());
        assertEquals(_30Y_CONCERT_DETAILS_DTO.eventId(), eventDetailsData.getData().eventId());
    }
}