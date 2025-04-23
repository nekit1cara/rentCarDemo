package com.crm.rentcar.Repository.Cars;

import com.crm.rentcar.Entity.Vehicle.CarInfo;
import com.crm.rentcar.Enums.CarType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarInfoRepository extends JpaRepository<CarInfo, Long> {

    Page<CarInfo> findByCarType(CarType carType, Pageable pageable);

    Page<CarInfo> findByCarBrand(String brand, Pageable pageable);

    boolean existsByCarModel(String model);

}
