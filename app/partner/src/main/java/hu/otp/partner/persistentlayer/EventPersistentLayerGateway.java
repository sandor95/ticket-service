package hu.otp.partner.persistentlayer;

import java.util.List;
import java.util.Optional;

import hu.otp.partner.common.model.Event;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class EventPersistentLayerGateway {

    private final EventRepository eventRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getById(@NotNull Long id) {
        return eventRepository.findById(id);
    }
}