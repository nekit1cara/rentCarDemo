package com.crm.rentcar.Entity.Vehicle;

import com.crm.rentcar.Enums.CarStatus;
import com.crm.rentcar.Enums.CarType;
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
public class CarInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CarStatus carStatus;

    @Enumerated(EnumType.STRING)
    private CarType carType;

    private String carBrand;

    private String carModel;

    private String carYear;

    private String carEngine;

    private Integer carSeats;

    private String carTransmissionType;

    @OneToOne
    @JoinColumn(name = "price_id", nullable = false)
    private CarPrice price;

    @JsonIgnore
    @OneToOne(mappedBy = "info", cascade = CascadeType.ALL)
    private Cars car;

}
