package com.crm.rentcar.Repository.Clients;

import com.crm.rentcar.Entity.Clients.Clients;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface ClientsRepository extends JpaRepository<Clients, Long> {

    Optional<Clients> findByClientInfoClientEmail(String email);

    boolean existsByClientInfoClientEmail(String mail);
}
