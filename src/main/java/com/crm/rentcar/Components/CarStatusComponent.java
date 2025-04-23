package com.crm.rentcar.Components;

import com.crm.rentcar.Entity.Orders.Orders;
import com.crm.rentcar.Entity.Vehicle.Cars;
import com.crm.rentcar.Enums.CarStatus;
import com.crm.rentcar.Enums.OrderStatus;
import com.crm.rentcar.Repository.Cars.CarsRepository;
import com.crm.rentcar.Repository.Orders.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class CarStatusComponent {

    private final OrdersRepository ordersRepository;
    private final CarsRepository carsRepository;

    @Autowired
    public CarStatusComponent(OrdersRepository ordersRepository, CarsRepository carsRepository) {
        this.ordersRepository = ordersRepository;
        this.carsRepository = carsRepository;
    }

    /**
     * Планировщик, который запускается каждый день в полночь.
     * Проверяет завершённые заказы и обновляет статус автомобиля на AVAILABLE,
     * если он всё ещё помечен как RESERVED.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateCarStatus() {

        // Получаем сегодняшнюю дату
        LocalDate today = LocalDate.now();

        // Находим все заказы, дата окончания которых раньше сегодняшней (то есть аренда завершена)
        List<Orders> endedOrders = ordersRepository.findByOrderEndDateBefore(today);

        // Проходимся по каждому завершённому заказу
        for (Orders order : endedOrders) {

            // Получаем машину, связанную с этим заказом
            Cars car = order.getOrderItems().getCars();

            // Если статус машины до сих пор RESERVED, меняем его на AVAILABLE
            if (car.getInfo().getCarStatus().equals(CarStatus.RESERVED)) {
                car.getInfo().setCarStatus(CarStatus.AVAILABLE);

                // Сохраняем обновлённый статус машины
                carsRepository.save(car);
            }

            order.setOrderStatus(OrderStatus.ENDED);
                ordersRepository.save(order);
        }
    }



}
