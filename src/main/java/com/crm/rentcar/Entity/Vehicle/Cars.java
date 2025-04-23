package com.crm.rentcar.Entity.Vehicle;

import com.crm.rentcar.Entity.Orders.OrderItems;
import com.crm.rentcar.Enums.CarStatus;
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
public class Cars {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "info_id", nullable = false)
    private CarInfo info;

    @JsonIgnore
    @OneToMany(mappedBy = "cars")
    private List<OrderItems> orderItems;

}
