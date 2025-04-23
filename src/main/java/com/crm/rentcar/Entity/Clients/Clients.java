package com.crm.rentcar.Entity.Clients;

import com.crm.rentcar.Entity.Orders.OrderItems;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Clients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "clientInfo_id") //, nullable = false
    private ClientInfo clientInfo;

    @JsonIgnore
    @OneToMany(mappedBy = "clients")
    private List<OrderItems> orderItems;
}
