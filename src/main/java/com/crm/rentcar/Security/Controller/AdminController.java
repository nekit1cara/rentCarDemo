package com.crm.rentcar.Security.Controller;

import com.crm.rentcar.Entity.Admin.AdminPermit;
import com.crm.rentcar.Security.Service.interfaces.AdminPermitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminPermitService adminPermitService;

    @Autowired
    public AdminController(AdminPermitService adminPermitService) {
        this.adminPermitService = adminPermitService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllAdmins(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return adminPermitService.getAllAdmins(page, size);
    }

    @GetMapping("/username")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAdminsByUsername(@RequestParam String username) {
        return adminPermitService.getAdminByUsername(username);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> addAdmin(@RequestBody AdminPermit admin) {
        return adminPermitService.addAdmin(admin);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteAdmin(@RequestParam String username) {
        return adminPermitService.deleteAdminByUsername(username);
    }

}
