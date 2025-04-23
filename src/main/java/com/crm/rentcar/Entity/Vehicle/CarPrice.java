package com.crm.rentcar.Entity.Vehicle;

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
public class CarPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer car_price1;

    private Integer car_price2;

    private Integer car_price3;

    private Integer car_price4;

    @JsonIgnore
    @OneToOne(mappedBy = "price", cascade = CascadeType.ALL)
    private CarInfo carInfo;

}
