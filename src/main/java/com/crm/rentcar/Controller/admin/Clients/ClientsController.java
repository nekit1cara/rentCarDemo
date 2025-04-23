package com.crm.rentcar.Controller.admin.Clients;

import com.crm.rentcar.Entity.Clients.Clients;
import com.crm.rentcar.Service.interfaces.ClientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("adminClientsController")
@RequestMapping("/api/admin/clients")
public class ClientsController {

    private final ClientsService clientsService;

    @Autowired
    public ClientsController(ClientsService clientsService) {
        this.clientsService = clientsService;
    }

    @GetMapping
    public ResponseEntity<?> getAllClients(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return clientsService.getAllClients(page, size);
    }

    @GetMapping("/id")
    public ResponseEntity<?> getClientById(@RequestParam Long clientId) {
        return clientsService.getClientById(clientId);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createClient(@RequestBody Clients client) {
        return clientsService.createClient(client);
    }

    @PutMapping("/update/id")
    public ResponseEntity<?> updateClientById(@RequestParam Long clientId,
                                              @RequestBody Clients client) {
        return clientsService.updateClientById(clientId, client);
    }

    @DeleteMapping("/delete/id")
    public ResponseEntity<?> deleteClientById(@RequestParam Long clientId) {
        return clientsService.deleteClient(clientId);
    }

}
