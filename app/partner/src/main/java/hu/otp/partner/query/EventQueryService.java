package hu.otp.partner.query;

import java.util.List;

import hu.otp.partner.common.model.Event;
import hu.otp.partner.exception.NoEntityFoundException;
import hu.otp.partner.persistentlayer.EventPersistentLayerGateway;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Transactional(readOnly = true)
public class EventQueryService {

    private final EventPersistentLayerGateway gateway;

    public List<Event> getAllEvents() {
        return gateway.getAllEvents();
    }

    public Event getById(@NotNull Long id) throws NoEntityFoundException {
        return gateway.getById(id).orElseThrow(() -> new NoEntityFoundException("No event found with ID: " + id.toString()));
    }

}