package com.crm.rentcar.Service.Admin.Interfaces;

import com.crm.rentcar.Entity.Vehicle.Cars;
import com.crm.rentcar.Enums.CarStatus;
import com.crm.rentcar.Enums.CarType;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CarsService {

    ResponseEntity<?> getAllCars(int page, int size);

    ResponseEntity<?> getCarById(Long carId);

    ResponseEntity<?> getCarsByType(int page, int size,CarType carType);

    ResponseEntity<?> getCarsByBrand(int page, int size,String brand);

    ResponseEntity<?> createCar(Cars cars);

    ResponseEntity<?> createCars(List<Cars> cars);

    ResponseEntity<?> updateCarById(Long carId, Cars cars);

    ResponseEntity<?> updateCarStatusById(Long carId, CarStatus carStatus);

    ResponseEntity<?> deleteCarById(Long carId);

}
