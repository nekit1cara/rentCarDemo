package com.crm.rentcar.Repository.Orders;

import com.crm.rentcar.Entity.Orders.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {


}
