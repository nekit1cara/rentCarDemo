package com.crm.rentcar.Repository.Orders;

import com.crm.rentcar.Entity.Orders.Orders;
import com.crm.rentcar.Entity.Vehicle.Cars;
import com.crm.rentcar.Enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    Optional<Orders> findByIdAndSessionId(Long id, String sessionId);

    Page<Orders> findBySessionId(String sessionId, Pageable pageable);

    Page<Orders> findBySessionIdAndOrderStatus(String sessionId, OrderStatus orderStatus, Pageable pageable);

    List<Orders> findByOrderItemsCarsAndOrderStartDateBeforeAndOrderEndDateAfter(Cars cars, LocalDate orderStartDate, LocalDate orderEndDate);

    List<Orders> findByOrderEndDateBefore(LocalDate orderEndDate);
}
