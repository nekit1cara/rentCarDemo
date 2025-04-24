package com.crm.rentcar.DTO.VehicleDTO;

import com.crm.rentcar.Entity.Vehicle.CarInfo;
import com.crm.rentcar.Entity.Vehicle.CarPrice;
import com.crm.rentcar.Entity.Vehicle.Cars;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarsDTO {

    private CarInfoDTO carInfoDTO;

    private CarPriceDTO carPrice;


    public static CarsDTO fromEntity(Cars cars) {

        if (cars == null) {
            return null;
        }

        CarInfo info = cars.getInfo();
        CarPrice price = info != null ? info.getPrice() : null;

        return new CarsDTO(
                info != null ? CarInfoDTO.fromEntity(info) : null,
                price != null ? CarPriceDTO.fromEntity(price) : null
        );

    }
}
