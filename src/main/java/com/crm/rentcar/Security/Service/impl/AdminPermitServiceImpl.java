package com.crm.rentcar.Security.Service.impl;

import com.crm.rentcar.Entity.Admin.AdminPermit;
import com.crm.rentcar.Exceptions.GlobalExceptions.CustomAlreadyExistException;
import com.crm.rentcar.Exceptions.GlobalExceptions.CustomNotFoundException;
import com.crm.rentcar.Security.Repository.AdminPermitRepository;
import com.crm.rentcar.Security.Service.interfaces.AdminPermitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminPermitServiceImpl implements AdminPermitService {

    private final AdminPermitRepository adminPermitRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminPermitServiceImpl(AdminPermitRepository adminPermitRepository) {
        this.adminPermitRepository = adminPermitRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public ResponseEntity<?> getAllAdmins(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminPermit> adminPermits = adminPermitRepository.findAll(pageable);
            return ResponseEntity.status(HttpStatus.OK).body(adminPermits);
    }

    @Override
    public ResponseEntity<?> getAdminByUsername(String username) {

        Optional<AdminPermit> adminToFind =  adminPermitRepository.findByAdminUsername(username);

            if (adminToFind.isEmpty()) {
                throw new CustomNotFoundException("Администратор не найден.");
            }

        AdminPermit existingAdmin = adminToFind.get();
            return ResponseEntity.status(HttpStatus.OK).body(existingAdmin);
    }

    @Override
    public ResponseEntity<?> addAdmin(AdminPermit admin) {

        if (adminPermitRepository.existsByAdminUsername(admin.getAdminUsername())) {
            throw new CustomAlreadyExistException("Admin already exists");
        }

        admin.setAdminPassword(passwordEncoder.encode(admin.getAdminPassword()));
        adminPermitRepository.save(admin);
            return ResponseEntity.status(HttpStatus.CREATED).body(admin);
    }

    @Override
    public ResponseEntity<?> deleteAdminByUsername(String username) {

        Optional<AdminPermit> adminToFind = adminPermitRepository.findByAdminUsername(username);

            if (adminToFind.isEmpty()) {
                throw new CustomNotFoundException("Admin : " + username + " does not exist");
            }

        AdminPermit adminToDelete = adminToFind.get();
            adminPermitRepository.delete(adminToDelete);
        return ResponseEntity.status(HttpStatus.OK).body("Admin deleted successfully.");

    }
}
