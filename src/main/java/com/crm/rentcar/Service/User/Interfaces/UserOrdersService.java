package com.crm.rentcar.Service.User.Interfaces;

import com.crm.rentcar.DTO.OrdersDTO.OrdersDTO;
import com.crm.rentcar.Entity.Orders.Orders;
import com.crm.rentcar.Enums.OrderStatus;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface UserOrdersService {

    Page<OrdersDTO> getAllOrders(HttpSession session, int page, int size);

    OrdersDTO getOrderById(HttpSession session, Long orderId);

    ResponseEntity<?> getOrdersByStatus(HttpSession session, OrderStatus status, int page, int size);

    ResponseEntity<?> createOrder(HttpSession session, Orders order);

    ResponseEntity<?> updateOrderById(HttpSession session, Long orderId, Orders order);

    ResponseEntity<?> deleteOrderById(HttpSession session, Long orderId);

}
