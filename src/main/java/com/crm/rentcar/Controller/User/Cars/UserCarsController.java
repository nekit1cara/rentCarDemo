package com.crm.rentcar.Controller.User.Cars;

import com.crm.rentcar.Enums.CarType;
import com.crm.rentcar.Service.User.Interfaces.UserCarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("userCarsController")
@RequestMapping("/api/cars")
public class UserCarsController {

    private final UserCarsService userCarsService;

    @Autowired
    public UserCarsController(UserCarsService userCarsService) {
        this.userCarsService = userCarsService;
    }

    @GetMapping
    public ResponseEntity<?> getAllCars(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return userCarsService.getAllCars(page, size);
    }

    @GetMapping("/id")
    public ResponseEntity<?> getCarById(@RequestParam Long carId) {
        return userCarsService.getCarById(carId);
    }

    @GetMapping("/type")
    public ResponseEntity<?> getCarTypeById(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam CarType carType) {
        return userCarsService.getCarsByType(page, size, carType);
    }

    @GetMapping("/brand")
    public ResponseEntity<?> getCarBrandById(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam String carBrand) {
        return userCarsService.getCarsByBrand(page, size, carBrand);
    }

}
