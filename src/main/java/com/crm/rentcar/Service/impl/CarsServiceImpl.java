package com.crm.rentcar.Service.impl;

import com.crm.rentcar.Entity.Vehicle.CarInfo;
import com.crm.rentcar.Entity.Vehicle.CarPrice;
import com.crm.rentcar.Entity.Vehicle.Cars;
import com.crm.rentcar.Enums.CarStatus;
import com.crm.rentcar.Enums.CarType;
import com.crm.rentcar.Exceptions.GlobalExceptions.CustomAlreadyExistException;
import com.crm.rentcar.Exceptions.GlobalExceptions.CustomCarPriceException;
import com.crm.rentcar.Exceptions.GlobalExceptions.CustomNotFoundException;
import com.crm.rentcar.Repository.Cars.CarInfoRepository;
import com.crm.rentcar.Repository.Cars.CarPriceRepository;
import com.crm.rentcar.Repository.Cars.CarsRepository;
import com.crm.rentcar.Service.interfaces.CarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CarsServiceImpl implements CarsService {

    private final CarsRepository carsRepository;
    private final CarInfoRepository carInfoRepository;
    private final CarPriceRepository carPriceRepository;

    @Autowired
    public CarsServiceImpl(CarsRepository carsRepository,
                           CarInfoRepository carInfoRepository,
                           CarPriceRepository carPriceRepository) {
        this.carsRepository = carsRepository;
        this.carInfoRepository = carInfoRepository;
        this.carPriceRepository = carPriceRepository;
    }

    @Override
    public ResponseEntity<?> getAllCars(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Cars> cars = carsRepository.findAll(pageable);
            return ResponseEntity.status(HttpStatus.OK).body(cars);
    }

    @Override
    public ResponseEntity<?> getCarById(Long carId) {

        Optional<Cars> car = carsRepository.findById(carId);

            if (car.isEmpty()) {
                throw new CustomNotFoundException("Автомобиль : " + carId + "  не найден.");
            }

        Cars foundedCar = car.get();
            return ResponseEntity.status(HttpStatus.OK).body(foundedCar);
    }

    @Override
    public ResponseEntity<?> getCarsByType(int page, int size,CarType carType) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CarInfo> cars = carInfoRepository.findByCarType(carType,pageable);

            if (cars.isEmpty()) {
                throw new CustomNotFoundException("Машины по типу : " + carType + "  не найдены.");
            }

        return ResponseEntity.status(HttpStatus.OK).body(cars);
    }

    @Override
    public ResponseEntity<?> getCarsByBrand(int page, int size,String brand) {

        Pageable pageable = PageRequest.of(page, size);
        Page<CarInfo> cars = carInfoRepository.findByCarBrand(brand,pageable);

        if (cars.isEmpty()) {
            throw new CustomNotFoundException("Машины бренда : " + brand + "  не найдены.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(cars);
    }

    @Override
    @Transactional
    public ResponseEntity<?> createCar(Cars car) {

            if (carInfoRepository.existsByCarModel(car.getInfo().getCarModel())) {
                throw new CustomAlreadyExistException("Автомобиль : " + car.getInfo().getCarBrand() +
                                                        " " + car.getInfo().getCarModel() + "  уже существует.");
            }

        CarPrice carPrice = createNewCarPrices(car);
        CarInfo carInfo = createNewCarInfo(car, carPrice);
            car.setInfo(carInfo);

        carsRepository.save(car);
            return ResponseEntity.status(HttpStatus.CREATED).body(car);
    }




    @Override
    @Transactional
    public ResponseEntity<?> createCars(List<Cars> cars) {

        for (Cars car : cars) {

            if (carInfoRepository.existsByCarModel(car.getInfo().getCarModel())) {
                throw new CustomAlreadyExistException("Автомобиль : " + car.getInfo().getCarBrand() +
                        " " + car.getInfo().getCarModel() + "  уже существует.");
            }


            CarPrice carPrice = createNewCarPrices(car);
            CarInfo carInfo = createNewCarInfo(car, carPrice);
            car.setInfo(carInfo);
        }

        carsRepository.saveAll(cars);
            return ResponseEntity.status(HttpStatus.CREATED).body(cars);

    }

    @Override
    @Transactional
    public ResponseEntity<?> updateCarById(Long carId, Cars car) {

        Optional<Cars> existingCar = carsRepository.findById(carId);

            if (existingCar.isEmpty()) {
                throw new CustomNotFoundException("Автомобиль : " + carId + "  не найден.");
            }
            if (carInfoRepository.existsByCarModel(car.getInfo().getCarModel())) {
                throw new CustomAlreadyExistException("Автомобиль : " + car.getInfo().getCarBrand() +
                        " " + car.getInfo().getCarModel() + "  уже существует.");
            }

        Cars carToUpdate = existingCar.get();
        CarInfo existingInfo = createNewCarInfo(car, carToUpdate);
        CarPrice existingPrice = createNewCarPrice(car, carToUpdate);


        carPriceRepository.save(existingPrice);
        carInfoRepository.save(existingInfo);
        carsRepository.save(carToUpdate);
            return ResponseEntity.status(HttpStatus.OK).body(carToUpdate);
    }




    @Override
    @Transactional
    public ResponseEntity<?> updateCarStatusById(Long carId, CarStatus carStatus) {

        Optional<Cars> existingCar = carsRepository.findById(carId);

            if (existingCar.isEmpty()) {
                throw new CustomNotFoundException("Автомобиль : " + carId + "  не найден.");
            }

        Cars carStatusToUpdate = existingCar.get();
            carStatusToUpdate.getInfo().setCarStatus(carStatus);
            carInfoRepository.save(carStatusToUpdate.getInfo());
        return ResponseEntity.status(HttpStatus.OK).body(carStatusToUpdate);
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteCarById(Long carId) {

        Optional<Cars> existingCar = carsRepository.findById(carId);

        if (existingCar.isEmpty()) {
            throw new CustomNotFoundException("Автомобиль : " + carId + "  не найден.");
        }

        Cars carToDelete = existingCar.get();
        CarInfo carInfoToDelete = carToDelete.getInfo();

        carPriceRepository.delete(carInfoToDelete.getPrice());
        carInfoToDelete.setPrice(null);

        carInfoRepository.delete(carInfoToDelete);
        carToDelete.setInfo(null);

        carsRepository.delete(carToDelete);
            return ResponseEntity.status(HttpStatus.OK).body("Автомобиль " + carId + " успешно удален.");
    }






/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

private CarPrice createNewCarPrices(Cars car) {
    CarPrice carPrice = new CarPrice();

    carPrice.setCar_price1(car.getInfo().getPrice().getCar_price1());
        if (carPrice.getCar_price1() <= 0) {
            throw new CustomCarPriceException("Цена автомобиля не может быть меньше или равно 0");
        }

    carPrice.setCar_price2(car.getInfo().getPrice().getCar_price2());
        if (carPrice.getCar_price2() <= 0) {
            throw new CustomCarPriceException("Цена автомобиля не может быть меньше или равно 0");

        }

    carPrice.setCar_price3(car.getInfo().getPrice().getCar_price3());
        if (carPrice.getCar_price3() <= 0) {
            throw new CustomCarPriceException("Цена автомобиля не может быть меньше или равно 0");
        }

    carPrice.setCar_price4(car.getInfo().getPrice().getCar_price4());
        if (carPrice.getCar_price4() <= 0) {
            throw new CustomCarPriceException("Цена автомобиля не может быть меньше или равно 0");
        }

    carPriceRepository.save(carPrice);
    return carPrice;
}

private CarInfo createNewCarInfo(Cars car, CarPrice carPrice) {
    CarInfo carInfo = new CarInfo();
    carInfo.setCarStatus(CarStatus.AVAILABLE);
    carInfo.setCarType(car.getInfo().getCarType());
    carInfo.setCarBrand(car.getInfo().getCarBrand());
    carInfo.setCarModel(car.getInfo().getCarModel());
    carInfo.setCarYear(car.getInfo().getCarYear());
    carInfo.setCarEngine(car.getInfo().getCarEngine());
    carInfo.setCarSeats(car.getInfo().getCarSeats());
    carInfo.setCarTransmissionType(car.getInfo().getCarTransmissionType());
    carInfo.setPrice(carPrice);

    carInfoRepository.save(carInfo);
    return carInfo;
}

private static CarInfo createNewCarInfo(Cars car, Cars carToUpdate) {
    CarInfo existingInfo = carToUpdate.getInfo();
    CarInfo newCarInfo = car.getInfo();

    if (newCarInfo.getCarStatus() != null) {
        existingInfo.setCarStatus(newCarInfo.getCarStatus());
    }
    if (newCarInfo.getCarType() != null) {
        existingInfo.setCarType(newCarInfo.getCarType());
    }
    if (newCarInfo.getCarBrand() != null) {
        existingInfo.setCarBrand(newCarInfo.getCarBrand());
    }
    if (newCarInfo.getCarModel() != null) {
        existingInfo.setCarModel(newCarInfo.getCarModel());
    }
    if (newCarInfo.getCarYear() != null) {
        existingInfo.setCarYear(newCarInfo.getCarYear());
    }
    if (newCarInfo.getCarEngine() != null) {
        existingInfo.setCarEngine(newCarInfo.getCarEngine());
    }
    if (newCarInfo.getCarSeats() != null) {
        existingInfo.setCarSeats(newCarInfo.getCarSeats());
    }
    if (newCarInfo.getCarTransmissionType() != null) {
        existingInfo.setCarTransmissionType(newCarInfo.getCarTransmissionType());
    }
    if (newCarInfo.getPrice() != null) {
        existingInfo.setPrice(newCarInfo.getPrice());
    }
    return existingInfo;
}

private static CarPrice createNewCarPrice(Cars car, Cars carToUpdate) {
    CarPrice existingPrice = carToUpdate.getInfo().getPrice();
    CarPrice newCarPrice = car.getInfo().getPrice();

    if (newCarPrice.getCar_price1() != null) {
        existingPrice.setCar_price1(newCarPrice.getCar_price1());
    }
    if (newCarPrice.getCar_price2() != null) {
        existingPrice.setCar_price2(newCarPrice.getCar_price2());
    }
    if (newCarPrice.getCar_price3() != null) {
        existingPrice.setCar_price3(newCarPrice.getCar_price3());
    }
    if (newCarPrice.getCar_price4() != null) {
        existingPrice.setCar_price4(newCarPrice.getCar_price4());
    }
    return existingPrice;
}


}
