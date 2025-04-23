package com.crm.rentcar.Entity.Orders;

import com.crm.rentcar.Entity.Clients.Clients;
import com.crm.rentcar.Entity.Vehicle.Cars;
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
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sessionId;

    @ManyToOne
    @JoinColumn(name = "clients_id", nullable = false)
    private Clients clients;

    @ManyToOne
    @JoinColumn(name = "cars_id", nullable = false)
    private Cars cars;


    @JsonIgnore
    @OneToOne(mappedBy = "orderItems", cascade = CascadeType.ALL)
    private Orders order;

}
