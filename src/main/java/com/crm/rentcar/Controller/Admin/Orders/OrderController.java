package com.crm.rentcar.Controller.Admin.Orders;

import com.crm.rentcar.Entity.Orders.Orders;
import com.crm.rentcar.Enums.OrderStatus;
import com.crm.rentcar.Service.Admin.Interfaces.OrdersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController("adminOrdersController")
@RequestMapping("/api/admin/orders")
public class OrderController {

    private final OrdersService ordersService;

    @Autowired
    public OrderController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping
    private Page<Orders> getOrders(HttpSession session ,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        return ordersService.getAllOrders(session, page, size);
    }

    @GetMapping("/id")
    private Orders getOrderById(HttpSession session,
                                @RequestParam Long orderId) {
        return ordersService.getOrderById(session,orderId);
    }

    @GetMapping("/status")
    private ResponseEntity<?> getOrderStatus(HttpSession session,
                                             @RequestParam OrderStatus status,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        return ordersService.getOrdersByStatus(session, status, page, size);
    }

    @PostMapping("/create")
    private ResponseEntity<?> createOrder(HttpSession session,
                                          @RequestBody Orders order) {
        return ordersService.createOrder(session, order);
    }

    @PutMapping("/update/id")
    private ResponseEntity<?> updateOrderById(HttpSession session,
                                              @RequestParam Long orderId,
                                              @RequestBody Orders order) {
        return ordersService.updateOrderById(session, orderId, order);
    }

    @DeleteMapping("/delete/id")
    private ResponseEntity<?> deleteOrderById(HttpSession session,
                                              @RequestParam Long orderId) {
        return ordersService.deleteOrderById(session, orderId);
    }


}
