package com.crm.rentcar.Repository.Cars;

import com.crm.rentcar.Entity.Vehicle.CarPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarPriceRepository extends JpaRepository<CarPrice, Long> {
}
