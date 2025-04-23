package com.crm.rentcar.Entity.Orders;

import com.crm.rentcar.Enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sessionId;


    private LocalDate orderStartDate;

    private LocalDate orderEndDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private Integer orderTotalPrice;

    @OneToOne
    @JoinColumn(name = "orderItems_id", nullable = false)
    private OrderItems orderItems;


}
