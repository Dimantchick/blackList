package ru.net.relay.blacklist.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "idx_domain", columnList = "domain")
})
@Builder
@AllArgsConstructor
public class VPNDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "domain_seq")
    @SequenceGenerator(name = "domain_seq", sequenceName = "domain_sequence", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String domain;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "TEXT DEFAULT ''")
    private String comment = "";
}
