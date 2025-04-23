package com.crm.rentcar.Controller.user.Cars;

import com.crm.rentcar.Enums.CarType;
import com.crm.rentcar.Service.admin.interfaces.CarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("userCarsController")
@RequestMapping("/api/cars")
public class CarsController {

    private final CarsService carsService;

    @Autowired
    public CarsController(CarsService carsService) {
        this.carsService = carsService;
    }

    @GetMapping
    public ResponseEntity<?> getAllCars(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return carsService.getAllCars(page, size);
    }

    @GetMapping("/id")
    public ResponseEntity<?> getCarById(@RequestParam Long carId) {
        return carsService.getCarById(carId);
    }

    @GetMapping("/type")
    public ResponseEntity<?> getCarTypeById(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam CarType carType) {
        return carsService.getCarsByType(page, size, carType);
    }

    @GetMapping("/brand")
    public ResponseEntity<?> getCarBrandById(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam String carBrand) {
        return carsService.getCarsByBrand(page, size, carBrand);
    }

}
