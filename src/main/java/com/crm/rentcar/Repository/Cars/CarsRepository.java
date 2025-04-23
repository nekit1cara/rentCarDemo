package com.crm.rentcar.Repository.Cars;

import com.crm.rentcar.Entity.Vehicle.Cars;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarsRepository extends JpaRepository<Cars, Long> {
}
