package com.crm.rentcar.Service.Admin.Impl;

import com.crm.rentcar.Entity.Clients.ClientInfo;
import com.crm.rentcar.Entity.Clients.Clients;
import com.crm.rentcar.Entity.Orders.OrderItems;
import com.crm.rentcar.Entity.Orders.Orders;
import com.crm.rentcar.Entity.Vehicle.CarPrice;
import com.crm.rentcar.Entity.Vehicle.Cars;
import com.crm.rentcar.Enums.CarStatus;
import com.crm.rentcar.Enums.OrderStatus;
import com.crm.rentcar.Exceptions.GlobalExceptions.CustomAlreadyExistException;
import com.crm.rentcar.Exceptions.GlobalExceptions.CustomNotFoundException;
import com.crm.rentcar.Exceptions.GlobalExceptions.CustomOrderDateFormatException;
import com.crm.rentcar.Repository.Cars.CarsRepository;
import com.crm.rentcar.Repository.Clients.ClientInfoRepository;
import com.crm.rentcar.Repository.Clients.ClientsRepository;
import com.crm.rentcar.Repository.Orders.OrderItemsRepository;
import com.crm.rentcar.Repository.Orders.OrdersRepository;
import com.crm.rentcar.Service.Admin.Interfaces.OrdersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final ClientsRepository clientsRepository;
    private final CarsRepository carsRepository;
    private final ClientInfoRepository clientInfoRepository;

    @Autowired
    public OrderServiceImpl(OrdersRepository ordersRepository,
                            OrderItemsRepository orderItemsRepository,
                            ClientsRepository clientsRepository,
                            CarsRepository carsRepository, ClientInfoRepository clientInfoRepository) {
        this.ordersRepository = ordersRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.clientsRepository = clientsRepository;
        this.carsRepository = carsRepository;
        this.clientInfoRepository = clientInfoRepository;
    }


    @Override
    public Page<Orders> getAllOrders(HttpSession session, int page, int size) {
       return getOrders(session,page,size);
    }

    @Override
    public Orders getOrderById(HttpSession session, Long orderId) {
        return getOrder(session, orderId);
    }

    @Override
    public ResponseEntity<?> getOrdersByStatus(HttpSession session, OrderStatus status, int page, int size) {

        String sessionId = session.getId();
        Pageable pageable = PageRequest.of(page, size);
        Page<Orders> allStatusOrders = ordersRepository.findBySessionIdAndOrderStatus(sessionId, status, pageable);

            if (allStatusOrders.getTotalElements() > 0) {
                throw new CustomNotFoundException("Заказов с таким статусом : " + status.name() + " не найдено.");
            }

        return ResponseEntity.status(HttpStatus.OK).body(allStatusOrders);
    }

    @Override
    @Transactional
    public ResponseEntity<?> createOrder(HttpSession session, Orders order) {

        order.setSessionId(session.getId());
        order.setOrderStatus(OrderStatus.APPROVED);
        order.setOrderStartDate(order.getOrderStartDate());

        checkOrderDateRule(order);

        OrderItems orderItems = new OrderItems();
            orderItems.setSessionId(order.getSessionId());

        Clients client = getOrCreateClient(order);
            orderItems.setClients(client);

            if (order.getOrderItems().getCars() == null) {
                throw new CustomNotFoundException("Машина не указана в заказе");
            }


        Long carId = order.getOrderItems().getCars().getId();
        Cars car = carsRepository.findById(carId)
                .orElseThrow(() -> new  CustomNotFoundException("Автомобиль : " + carId + " не найден."));


        CarStatus carStatus = car.getInfo().getCarStatus();
            switch (carStatus) {
                case RESERVED -> throw new CustomAlreadyExistException("Автомобиль " + car.getInfo().getCarBrand() +
                        " " + car.getInfo().getCarModel()  + " на данный момент зарезервирован");

                case UNAVAILABLE -> throw new CustomAlreadyExistException("Автомобиль " + car.getInfo().getCarBrand() +
                        " " + car.getInfo().getCarModel()  + " на данный момент не доступен");
            }

        List<Orders> overlappingDays = ordersRepository
                .findByOrderItemsCarsAndOrderStartDateBeforeAndOrderEndDateAfter(car, order.getOrderStartDate(), order.getOrderEndDate());

            if (!overlappingDays.isEmpty()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Авто на данный период занято.");
            }

        car.getInfo().setCarStatus(CarStatus.RESERVED);
            carsRepository.save(car);

        orderItems.setCars(car);
        order.setOrderItems(orderItems);
        orderItemsRepository.save(orderItems);

        Long betweenDays = ChronoUnit.DAYS.between(order.getOrderStartDate(), order.getOrderEndDate());

        int totalPrice = calculcateOrderTotalPrice(betweenDays,car.getInfo().getPrice());
            order.setOrderTotalPrice(totalPrice);

        ordersRepository.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }


    @Override
    @Transactional
    public ResponseEntity<?> updateOrderById(HttpSession session, Long orderId, Orders order) {

        String sessionId = session.getId();

        Optional<Orders> existingOrder = ordersRepository.findByIdAndSessionId(orderId, sessionId);

            if (existingOrder.isEmpty()) {
                throw new CustomNotFoundException("Заказ : " + orderId + " не найден.");
            }

        Orders orderToUpdate = existingOrder.get();
        OrderItems newOrderItems = order.getOrderItems();

            if (order.getOrderStartDate() != null) {
                orderToUpdate.setOrderStartDate(order.getOrderStartDate());
            }
            if (order.getOrderEndDate() != null) {
                orderToUpdate.setOrderEndDate(order.getOrderEndDate());
            }
            if (order.getOrderStatus() != null) {
                orderToUpdate.setOrderStatus(order.getOrderStatus());
            }
            if (order.getOrderTotalPrice() != null) {
                orderToUpdate.setOrderTotalPrice(order.getOrderTotalPrice());
            }

            if (newOrderItems != null) {
                // Сохраняем машины/клиентов, если это новые сущности (без id)
                if (newOrderItems.getCars() != null && newOrderItems.getCars().getId() == null) {
                    carsRepository.save(newOrderItems.getCars());
                }
                if (newOrderItems.getClients() != null && newOrderItems.getClients().getId() == null) {
                    clientsRepository.save(newOrderItems.getClients());
                }

                newOrderItems.setOrder(orderToUpdate); // связь обратная
                orderItemsRepository.save(newOrderItems); // сохраняем до добавления в заказ
                orderToUpdate.setOrderItems(newOrderItems);
            }

        ordersRepository.save(orderToUpdate);
        return ResponseEntity.status(HttpStatus.OK).body("Заказ : " + orderId + " успешно изменен.");
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteOrderById(HttpSession session, Long orderId) {

        Orders order = getOrder(session, orderId);
        OrderItems orderItems = order.getOrderItems();


        Clients clients = order.getOrderItems().getClients();

            if (clients != null) {
                orderItems.setClients(null);

                if (clients.getOrderItems().isEmpty()) {
                    clientsRepository.delete(clients);
                }

            }

            if (order.getOrderItems().getCars() != null) {
                orderItems.setCars(null);
            }

        orderItemsRepository.delete(orderItems);
            order.setOrderItems(null);

        ordersRepository.delete(order);
            return ResponseEntity.status(HttpStatus.OK).body("Заказ : " + order.getId() + " успешно удален.");
    }


///////////////////////////////////////////////////////////////////        PRIVATE METHODS     ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

private Orders getOrder(HttpSession session, Long orderId) {
        String sessionId = session.getId();

        Optional<Orders> foundedOrder = ordersRepository.findByIdAndSessionId(orderId,sessionId);

            if (foundedOrder.isEmpty()) {
                throw new CustomNotFoundException("Заказ : " + orderId + " не найден.");
            } else {
                return foundedOrder.get();
            }
}

private Page<Orders> getOrders(HttpSession session, int page, int size) {

    String sessionId = session.getId();

    Pageable pageable = PageRequest.of(page, size);
        Page<Orders> ordersList = ordersRepository.findBySessionId(sessionId, pageable);

        if (ordersList.isEmpty()) {
            throw new CustomNotFoundException("Заказы не найдены.");
        } else {
            return ordersList;
        }
}

private Clients getOrCreateClient(Orders order) {

        if (clientInfoRepository.existsByClientEmail(order.getOrderItems().getClients().getClientInfo().getClientEmail())) {

            String existingClientMail = order.getOrderItems().getClients().getClientInfo().getClientEmail();

            Clients client = clientsRepository.findByClientInfoClientEmail(existingClientMail)
                    .orElseThrow(() -> new CustomNotFoundException("Клиент с почтой: " + existingClientMail + "  не найден."));
            order.getOrderItems().setClients(client);
                return client;
        } else {
            Clients client = new Clients();

            ClientInfo clientInfo = new ClientInfo();
            clientInfo.setClientFirstName(order.getOrderItems().getClients().getClientInfo().getClientFirstName());
            clientInfo.setClientLastName(order.getOrderItems().getClients().getClientInfo().getClientLastName());
            clientInfo.setClientEmail(order.getOrderItems().getClients().getClientInfo().getClientEmail());
            clientInfo.setClientPhone(order.getOrderItems().getClients().getClientInfo().getClientPhone());
            clientInfo.setDrivingExperience(order.getOrderItems().getClients().getClientInfo().getDrivingExperience());
                clientInfoRepository.save(clientInfo);

            client.setClientInfo(clientInfo);
                clientsRepository.save(client);
            return client;
        }
}

private int calculcateOrderTotalPrice(Long betweenDays, CarPrice carPrice) {

    if (carPrice == null) {
        throw new CustomNotFoundException("Диапазон цен на авто не найден.");
    }

    int totalPrice = 0;

    if (betweenDays == 0) {
        throw new CustomOrderDateFormatException("Количество дней не может быть равна 0");
    } else if (betweenDays >= 0 && betweenDays <= 5) {
        totalPrice = carPrice.getCar_price1() * betweenDays.intValue();
    } else if (betweenDays >= 6 && betweenDays <= 10) {
        totalPrice = carPrice.getCar_price2() * betweenDays.intValue();
    } else if (betweenDays >= 11 && betweenDays <= 25) {
        totalPrice = carPrice.getCar_price3() * betweenDays.intValue();
    } else if (betweenDays >= 26) {
        totalPrice = carPrice.getCar_price4() * betweenDays.intValue();
    }

    return totalPrice;
}


private static void checkOrderDateRule(Orders order) {

    if (order.getOrderStartDate() == null || order.getOrderEndDate() == null) {
        throw new CustomOrderDateFormatException("Дата начала и окончания бронирования не должны быть пустыми.");
    }

    if (order.getOrderStartDate().isBefore(LocalDate.now())) {
        throw new CustomOrderDateFormatException("Дата начала бронирования автомобиля не может быть меньше сегодняшней даты.");
    }

    if (order.getOrderEndDate().isBefore(LocalDate.now()) || order.getOrderEndDate().equals(LocalDate.now())) {
        throw new CustomOrderDateFormatException("Дата окончания бронирования авто , не может быть равна дате начала бронирования авто" +
                "\n или меньше чем дата начала бронирования авто");
    } else {
        order.setOrderEndDate(order.getOrderEndDate());
    }

    if (order.getOrderStartDate().isAfter(order.getOrderEndDate())) {
        throw new CustomOrderDateFormatException("Дата начала бронирования автомобиля не может быть позже  даты окончания.");
    }
}


}
