package ru.net.relay.blacklist.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(indexes = @Index(name = "idx_adminuser_username", columnList = "username"))
@Builder
@AllArgsConstructor
public class AdminUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_sequence", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(length = 50)
    private String username;

    private String password;

}
