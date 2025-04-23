package com.crm.rentcar.Controller.admin.Cars;

import com.crm.rentcar.Entity.Vehicle.Cars;
import com.crm.rentcar.Enums.CarStatus;
import com.crm.rentcar.Enums.CarType;
import com.crm.rentcar.Service.interfaces.CarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminCarsController")
@RequestMapping("/api/admin/cars")
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

    @PostMapping("/create")
    public ResponseEntity<?> createCar(@RequestBody Cars cars) {
        return carsService.createCar(cars);
    }

    @PostMapping("/create-list")
    public ResponseEntity<?> createCarList(@RequestBody List<Cars> carsList) {
        return carsService.createCars(carsList);
    }

    @PutMapping("/update/")
    public ResponseEntity<?> updateCarById(@RequestParam Long carId,
                                           @RequestBody Cars cars) {
        return carsService.updateCarById(carId, cars);
    }

    @PutMapping("/update/status/")
    public ResponseEntity<?> updateCarStatusId(@RequestParam Long carId,
                                               @RequestParam CarStatus carStatus) {
        return carsService.updateCarStatusById(carId, carStatus);
    }

    @DeleteMapping("/delete/")
    public ResponseEntity<?> deleteCarById(@RequestParam Long carId) {
        return carsService.deleteCarById(carId);
    }

}
