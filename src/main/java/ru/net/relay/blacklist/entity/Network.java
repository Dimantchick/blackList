package ru.net.relay.blacklist.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(indexes = @Index(name = "idx_network", columnList = "network"))
@Builder
@AllArgsConstructor
public class Network {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "network_seq")
    @SequenceGenerator(name = "network_seq", sequenceName = "network_sequence", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(length = 20, unique = true, nullable = false)
    private String network;

    @Builder.Default
    private Boolean manual = true;

    @Builder.Default
    private Boolean active = true;

    @Builder.Default
    private Boolean imported = false;
}
