package com.crm.rentcar.Controller.User.Orders;

import com.crm.rentcar.DTO.OrdersDTO.OrdersDTO;
import com.crm.rentcar.Entity.Orders.Orders;
import com.crm.rentcar.Enums.OrderStatus;
import com.crm.rentcar.Service.User.Interfaces.UserOrdersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController("userOrdersController")
@RequestMapping("/api/orders")
public class UserOrderController {

    private final UserOrdersService userOrdersService;

    @Autowired
    public UserOrderController(UserOrdersService userOrdersService) {
        this.userOrdersService = userOrdersService;
    }

    @GetMapping
    private Page<OrdersDTO> getOrders(HttpSession session ,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        return userOrdersService.getAllOrders(session, page, size);
    }

    @GetMapping("/id")
    private OrdersDTO getOrderById(HttpSession session,
                                @RequestParam Long orderId) {
        return userOrdersService.getOrderById(session,orderId);
    }

    @GetMapping("/status")
    private ResponseEntity<?> getOrderStatus(HttpSession session,
                                             @RequestParam OrderStatus status,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        return userOrdersService.getOrdersByStatus(session, status, page, size);
    }

    @PostMapping("/create")
    private ResponseEntity<?> createOrder(HttpSession session,
                                          @RequestBody Orders order) {
        return userOrdersService.createOrder(session, order);
    }

}
