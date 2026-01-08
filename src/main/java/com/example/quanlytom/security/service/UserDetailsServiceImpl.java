package com.example.quanlytom.security.service;

import com.example.quanlytom.entity.Users;
import com.example.quanlytom.repository.UsersRepository;
import com.example.quanlytom.security.model.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersRepository userRepository;

    // constructor injection (recommended)
    public UserDetailsServiceImpl(UsersRepository userRepository) {
        this.userRepository = userRepository;
    }

    // transactional read-only
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {

        Users user = userRepository.findByPhoneOrEmail(identifier,identifier)
                .orElseThrow(() -> new UsernameNotFoundException("User not found" ));

        return UserDetailsImpl.build(user);
    }
}
