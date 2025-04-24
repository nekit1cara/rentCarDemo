package com.crm.rentcar.DTO.VehicleDTO;

import com.crm.rentcar.Entity.Vehicle.CarInfo;
import com.crm.rentcar.Enums.CarStatus;
import com.crm.rentcar.Enums.CarType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarInfoDTO {


    @Enumerated(EnumType.STRING)
    private CarStatus carStatus;

    @Enumerated(EnumType.STRING)
    private CarType carType;

    private String carBrand;

    private String carModel;

    private String carYear;

    private String carEngine;

    private Integer carSeats;

    private String carTransmissionType;

    public static  CarInfoDTO fromEntity(CarInfo carInfo) {

        if (carInfo == null) {
            return null;
        }

        return new CarInfoDTO(
                carInfo.getCarStatus(),
                carInfo.getCarType(),
                carInfo.getCarBrand(),
                carInfo.getCarModel(),
                carInfo.getCarYear(),
                carInfo.getCarEngine(),
                carInfo.getCarSeats(),
                carInfo.getCarTransmissionType()
        );
    }


}
