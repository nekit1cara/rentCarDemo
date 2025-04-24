package com.crm.rentcar.DTO.ClientsDTO;

import com.crm.rentcar.Entity.Clients.ClientInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientInfoDTO {

    private String clientFirstName;

    private String clientLastName;

    private String clientEmail;

    private String clientPhone;

    private Integer drivingExperience;

    public static ClientInfoDTO fromEntity(ClientInfo clientInfo) {

        if (clientInfo == null) {
            return null;
        }

        return new ClientInfoDTO(
                clientInfo.getClientFirstName(),
                clientInfo.getClientLastName(),
                clientInfo.getClientEmail(),
                clientInfo.getClientPhone(),
                clientInfo.getDrivingExperience()
        );
    }

}
