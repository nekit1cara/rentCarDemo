package com.crm.rentcar.DTO.VehicleDTO;

import com.crm.rentcar.Entity.Vehicle.CarPrice;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarPriceDTO {

    private Integer car_price1;

    private Integer car_price2;

    private Integer car_price3;

    private Integer car_price4;

    public static CarPriceDTO fromEntity(CarPrice carPrice) {

        if (carPrice == null) {
            return null;
        }

        return new CarPriceDTO(
                carPrice.getCar_price1(),
                carPrice.getCar_price2(),
                carPrice.getCar_price3(),
                carPrice.getCar_price4()
        );

    }
}
