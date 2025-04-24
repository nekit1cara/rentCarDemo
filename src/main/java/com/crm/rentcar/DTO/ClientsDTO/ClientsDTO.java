package com.crm.rentcar.DTO.ClientsDTO;

import com.crm.rentcar.Entity.Clients.ClientInfo;
import com.crm.rentcar.Entity.Clients.Clients;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientsDTO {

    private ClientInfoDTO clientInfoDTO;

    public static ClientsDTO fromEntity(Clients clients) {

        if (clients == null) {
            return null;
        }

        ClientInfo info = clients.getClientInfo();

        return new ClientsDTO(
            info != null ? ClientInfoDTO.fromEntity(info) : null
        );

    }

}
