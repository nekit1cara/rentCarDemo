package com.crm.rentcar.Security.Repository;

import com.crm.rentcar.Entity.Admin.AdminPermit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminPermitRepository extends JpaRepository<AdminPermit,Long> {

    Optional<AdminPermit> findByAdminUsername(String username);

    boolean existsByAdminUsername(String username);

}
