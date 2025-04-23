package com.crm.rentcar.Security.Service.impl;

import com.crm.rentcar.Entity.Admin.AdminPermit;
import com.crm.rentcar.Security.Repository.AdminPermitRepository;
import com.crm.rentcar.Security.UserDetails.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AdminPermitRepository adminPermitRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<AdminPermit> adminPermit = adminPermitRepository.findByAdminUsername(username);
        return adminPermit.map(MyUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Admin " + username + " not found"));
    }

}
