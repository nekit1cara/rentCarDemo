package com.crm.rentcar.Repository.Orders;

import com.crm.rentcar.Entity.Orders.OrderItems;
import com.crm.rentcar.Entity.Vehicle.Cars;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {


}
