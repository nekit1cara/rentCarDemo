package com.crm.rentcar.DTO.OrdersDTO;

import com.crm.rentcar.Entity.Orders.OrderItems;
import com.crm.rentcar.Entity.Orders.Orders;
import com.crm.rentcar.Enums.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class OrdersDTO {

    private LocalDate orderStartDate;

    private LocalDate orderEndDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private Integer orderTotalPrice;

    private OrderItemsDTO orderItemsDTO;

    public static OrdersDTO fromEntity(Orders order) {

            if (order == null) {
                return null;
            }

        OrderItems orderItems = order.getOrderItems();

            return new OrdersDTO(
                    order.getOrderStartDate(),
                    order.getOrderEndDate(),
                    order.getOrderStatus(),
                    order.getOrderTotalPrice(),
                    orderItems != null ? OrderItemsDTO.fromEntity(orderItems) : null
            );

    }
}
