package com.crm.rentcar.Repository.Clients;

import com.crm.rentcar.Entity.Clients.ClientInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientInfoRepository extends JpaRepository<ClientInfo, Long> {

    boolean existsByClientEmail(String clientEmail);


}
