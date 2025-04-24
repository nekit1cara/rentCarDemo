package com.crm.rentcar.Service.Admin.Interfaces;

import com.crm.rentcar.Entity.Clients.Clients;
import org.springframework.http.ResponseEntity;

public interface ClientsService {

    ResponseEntity<?> getAllClients(int page, int size);

    ResponseEntity<?> getClientById(Long id);

    ResponseEntity<?> createClient(Clients client);

    ResponseEntity<?> updateClientById(Long clientId, Clients client);

    ResponseEntity<?> deleteClient(Long id);

}
