package com.crm.rentcar.Service.User.Interfaces;

import com.crm.rentcar.Enums.CarType;
import org.springframework.http.ResponseEntity;



public interface UserCarsService {

    ResponseEntity<?> getAllCars(int page, int size);

    ResponseEntity<?> getCarById(Long carId);

    ResponseEntity<?> getCarsByType(int page, int size,CarType carType);

    ResponseEntity<?> getCarsByBrand(int page, int size,String brand);


}
