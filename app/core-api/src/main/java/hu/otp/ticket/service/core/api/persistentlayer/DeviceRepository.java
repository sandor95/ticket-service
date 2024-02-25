package hu.otp.ticket.service.core.api.persistentlayer;

import hu.otp.ticket.service.core.api.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    @Query("""
        select count(d) > 0 from Device d
        join d.user u
        where d.deviceHash = :deviceHash
        and u.id = :userId
        """)
    boolean isDeviceAssignedToUser(Long userId, String deviceHash);
}