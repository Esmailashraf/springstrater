package org.studyeasy.springstrater.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.studyeasy.springstrater.Model.Authority;
import org.studyeasy.springstrater.Repositories.AuthorityRepository;

@Service
public class AuthorityService {
    @Autowired
    private AuthorityRepository authorityRepository;
    
    public Authority save(@NonNull Authority authority) {
        return authorityRepository.save(authority);
    }
    public Optional<Authority> findById(@NonNull Long id) {
        return authorityRepository.findById(id);
    }
}
