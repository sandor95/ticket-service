package hu.otp.ticket.service.core.api.persistentlayer;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class DeviceGateway {

    private final DeviceRepository repository;

    public boolean isDeviceAssignedToUser(@NotNull Long userId, @NotNull String deviceHash) {
        return repository.isDeviceAssignedToUser(userId, deviceHash);
    }
}