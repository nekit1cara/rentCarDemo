package com.crm.rentcar.Entity.Clients;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ClientInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientFirstName;

    private String clientLastName;

    private String clientEmail;

    private String clientPhone;

    private Integer drivingExperience;

    @JsonIgnore
    @OneToOne(mappedBy = "clientInfo", cascade = CascadeType.ALL)
    private Clients clients;

}
