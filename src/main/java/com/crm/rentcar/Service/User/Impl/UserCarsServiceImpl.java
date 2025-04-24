package com.crm.rentcar.Service.User.Impl;

import com.crm.rentcar.DTO.VehicleDTO.CarInfoDTO;
import com.crm.rentcar.DTO.VehicleDTO.CarsDTO;
import com.crm.rentcar.Entity.Vehicle.CarInfo;
import com.crm.rentcar.Entity.Vehicle.Cars;
import com.crm.rentcar.Enums.CarType;
import com.crm.rentcar.Exceptions.GlobalExceptions.CustomNotFoundException;
import com.crm.rentcar.Repository.Cars.CarInfoRepository;
import com.crm.rentcar.Repository.Cars.CarsRepository;
import com.crm.rentcar.Service.User.Interfaces.UserCarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCarsServiceImpl implements UserCarsService {

    private final CarsRepository carsRepository;
    private final CarInfoRepository carInfoRepository;


    @Autowired
    public UserCarsServiceImpl(CarsRepository carsRepository,
                               CarInfoRepository carInfoRepository) {
        this.carsRepository = carsRepository;
        this.carInfoRepository = carInfoRepository;
    }

    @Override
    public ResponseEntity<?> getAllCars(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Cars> cars = carsRepository.findAll(pageable);

        Page<CarsDTO> carsDTO = cars.map(CarsDTO::fromEntity);
            return ResponseEntity.status(HttpStatus.OK).body(carsDTO);
    }

    @Override
    public ResponseEntity<?> getCarById(Long carId) {

        Optional<Cars> car = carsRepository.findById(carId);

            if (car.isEmpty()) {
                throw new CustomNotFoundException("Автомобиль : " + carId + "  не найден.");
            }

        Cars foundedCar = car.get();

        CarsDTO carsDTO = CarsDTO.fromEntity(foundedCar);
            return ResponseEntity.status(HttpStatus.OK).body(carsDTO);
    }

    @Override
    public ResponseEntity<?> getCarsByType(int page, int size,CarType carType) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CarInfo> cars = carInfoRepository.findByCarType(carType,pageable);

            if (cars.isEmpty()) {
                throw new CustomNotFoundException("Машины по типу : " + carType + "  не найдены.");
            }

        Page<CarInfoDTO> carInfoDTO = cars.map(CarInfoDTO::fromEntity);
            return ResponseEntity.status(HttpStatus.OK).body(carInfoDTO);
    }

    @Override
    public ResponseEntity<?> getCarsByBrand(int page, int size,String brand) {

        Pageable pageable = PageRequest.of(page, size);
        Page<CarInfo> cars = carInfoRepository.findByCarBrand(brand,pageable);

        if (cars.isEmpty()) {
            throw new CustomNotFoundException("Машины бренда : " + brand + "  не найдены.");
        }

        Page<CarInfoDTO> carInfoDTO = cars.map(CarInfoDTO::fromEntity);
            return ResponseEntity.status(HttpStatus.OK).body(carInfoDTO);
    }


}
