package ru.net.relay.blacklist.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class DbParam {

    @Id
    @Column(name = "param_name")
    private String name;

    @Column(name = "param_value")
    private String value;

}
