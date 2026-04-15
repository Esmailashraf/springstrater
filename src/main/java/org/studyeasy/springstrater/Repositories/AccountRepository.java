package org.studyeasy.springstrater.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.studyeasy.springstrater.Model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    Optional<Account> findOneByEmailIgnoreCase(String email);

    Optional<Account> findByResetPasswordToken(String token);
} 