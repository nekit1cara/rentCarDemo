package com.crm.rentcar.Service.Admin.Impl;

import com.crm.rentcar.Entity.Clients.ClientInfo;
import com.crm.rentcar.Entity.Clients.Clients;
import com.crm.rentcar.Exceptions.GlobalExceptions.CustomAlreadyExistException;
import com.crm.rentcar.Exceptions.GlobalExceptions.CustomNotFoundException;
import com.crm.rentcar.Repository.Clients.ClientInfoRepository;
import com.crm.rentcar.Repository.Clients.ClientsRepository;
import com.crm.rentcar.Service.Admin.Interfaces.ClientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClientsServiceImpl implements ClientsService {

    private final ClientsRepository clientsRepository;
    private final ClientInfoRepository clientInfoRepository;

    @Autowired
    public ClientsServiceImpl(ClientsRepository clientsRepository,
                              ClientInfoRepository clientInfoRepository) {
        this.clientsRepository = clientsRepository;
        this.clientInfoRepository = clientInfoRepository;
    }


    @Override
    public ResponseEntity<?> getAllClients(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Clients> clients = clientsRepository.findAll(pageable);
            return ResponseEntity.status(HttpStatus.OK).body(clients);
    }

    @Override
    public ResponseEntity<?> getClientById(Long id) {

        Clients client = clientsRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Клиент : " + id + " не найден."));

        return ResponseEntity.status(HttpStatus.OK).body(client);
    }

    @Override
    @Transactional
    public ResponseEntity<?> createClient(Clients client) {

        if (clientsRepository.existsByClientInfoClientEmail(client.getClientInfo().getClientEmail())) {
            throw new CustomAlreadyExistException("Клиент c почтой : " + client.getClientInfo().getClientEmail() + " уже существует.");
        }

        ClientInfo clientInfo = createNewClientInfo(client);
            client.setClientInfo(clientInfo);
            clientInfoRepository.save(clientInfo);

        clientsRepository.save(client);
            return ResponseEntity.status(HttpStatus.CREATED).body(client);

    }



    @Override
    @Transactional
    public ResponseEntity<?> updateClientById(Long clientId, Clients client) {

        Optional<Clients> clientToFind = clientsRepository.findById(clientId);

            if (clientToFind.isEmpty()) {
                throw new CustomNotFoundException("Клиент : " +  clientId + " не найден.");
            }

        Clients clientToUpdate = clientToFind.get();
        ClientInfo clientInfoToUpdate = clientToUpdate.getClientInfo();

            if (client.getClientInfo().getClientFirstName() != null) {
                clientInfoToUpdate.setClientFirstName(client.getClientInfo().getClientFirstName());
            }
            if (client.getClientInfo().getClientLastName() != null) {
                clientInfoToUpdate.setClientLastName(client.getClientInfo().getClientLastName());
            }
            if (client.getClientInfo().getClientEmail() != null) {
                clientInfoToUpdate.setClientEmail(client.getClientInfo().getClientEmail());
            }
            if (client.getClientInfo().getClientPhone() != null) {
                clientInfoToUpdate.setClientPhone(client.getClientInfo().getClientPhone());
            }
            if (client.getClientInfo().getDrivingExperience() != null) {
                clientInfoToUpdate.setDrivingExperience(client.getClientInfo().getDrivingExperience());
            }

        clientInfoRepository.save(clientInfoToUpdate);
        clientsRepository.save(clientToUpdate);
            return ResponseEntity.status(HttpStatus.OK).body("Клиент : " + clientId + " успешно обновлен.");

    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteClient(Long id) {

        Optional<Clients> clientToFind = clientsRepository.findById(id);

        if (clientToFind.isEmpty()) {
            throw new CustomNotFoundException("Клиент : " +  id + " не найден.");
        }

        Clients clientToDelete = clientToFind.get();
            clientInfoRepository.delete(clientToDelete.getClientInfo());
            clientToDelete.setClientInfo(null);
            clientsRepository.delete(clientToDelete);

        return ResponseEntity.status(HttpStatus.OK).body("Клиент : " + id + " успешно удален.");
    }

////////////////////////////////////////////////////////////////        PRIVATE METHODS     ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

private static ClientInfo createNewClientInfo(Clients client) {
    ClientInfo clientInfo = new ClientInfo();
    clientInfo.setClientFirstName(client.getClientInfo().getClientFirstName());
    clientInfo.setClientLastName(client.getClientInfo().getClientLastName());
    clientInfo.setClientEmail(client.getClientInfo().getClientEmail());
    clientInfo.setClientPhone(client.getClientInfo().getClientPhone());
    clientInfo.setDrivingExperience(client.getClientInfo().getDrivingExperience());
    return clientInfo;
}

}
