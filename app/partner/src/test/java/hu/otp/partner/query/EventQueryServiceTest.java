package hu.otp.partner.query;

import static hu.otp.partner.knownobject.Events.BETON_HOFI_CONCERT;
import static hu.otp.partner.knownobject.Events._30Y_CONCERT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import hu.otp.partner.common.model.Event;
import hu.otp.partner.exception.NoEntityFoundException;
import hu.otp.partner.persistentlayer.EventPersistentLayerGateway;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class EventQueryServiceTest {

    @Mock
    EventPersistentLayerGateway gatewayMock;

    EventQueryService service;

    @BeforeEach
    void setUp() {
        service = new EventQueryService(gatewayMock);
    }

    @Test
    void shouldReturnAllEvents() {
        when(gatewayMock.getAllEvents()).thenReturn(List.of(_30Y_CONCERT, BETON_HOFI_CONCERT));

        List<Event> allEvents = service.getAllEvents();

        assertNotNull(allEvents);
        assertEquals(2, allEvents.size());
        assertTrue(allEvents.contains(_30Y_CONCERT));
        assertTrue(allEvents.contains(BETON_HOFI_CONCERT));
    }

    @Test
    void shouldReturnEventById() throws NoEntityFoundException {
        when(gatewayMock.getById(eq(BETON_HOFI_CONCERT.getId()))).thenReturn(Optional.of(BETON_HOFI_CONCERT));

        Event event = service.getById(BETON_HOFI_CONCERT.getId());

        assertEquals(BETON_HOFI_CONCERT, event);
    }

    @Test
    void shouldThrowExceptionWhenEventNotFound() {
        long id = 9L;
        when(gatewayMock.getById(eq(id))).thenReturn(Optional.empty());

        NoEntityFoundException exception = assertThrows(NoEntityFoundException.class, () -> service.getById(id));

        assertEquals("No event found with ID: " + id, exception.getMessage());
    }
}