package com.crm.rentcar.DTO.OrdersDTO;

import com.crm.rentcar.DTO.ClientsDTO.ClientsDTO;
import com.crm.rentcar.DTO.VehicleDTO.CarsDTO;
import com.crm.rentcar.Entity.Clients.Clients;
import com.crm.rentcar.Entity.Orders.OrderItems;
import com.crm.rentcar.Entity.Vehicle.Cars;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemsDTO {

    private ClientsDTO clientsDTO;
    private CarsDTO carsDTO;

    public static OrderItemsDTO fromEntity(OrderItems orderItems) {

            if (orderItems == null) {
                return null;
            }

        Cars cars = orderItems.getCars();
        Clients clients = orderItems.getClients();


            return new OrderItemsDTO(
                clients != null ? ClientsDTO.fromEntity(clients) : null,
                cars != null ? CarsDTO.fromEntity(cars) : null
            );

    }
}
