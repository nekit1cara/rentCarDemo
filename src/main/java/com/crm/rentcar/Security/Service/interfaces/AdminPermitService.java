package com.crm.rentcar.Security.Service.interfaces;

import com.crm.rentcar.Entity.Admin.AdminPermit;
import org.springframework.http.ResponseEntity;

public interface AdminPermitService {

    ResponseEntity<?> getAllAdmins(int page, int size);

    ResponseEntity<?> getAdminByUsername(String username);

    ResponseEntity<?> addAdmin(AdminPermit admin);

}
