package hu.otp.ticket.service.core.api.model;

import hu.otp.ticket.service.model.DatabaseEntry;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DEVICE")
public class Device extends DatabaseEntry {

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "DEVICE_HASH", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String deviceHash;
}