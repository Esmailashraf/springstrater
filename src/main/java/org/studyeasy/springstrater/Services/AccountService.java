package org.studyeasy.springstrater.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.studyeasy.springstrater.Model.Account;
import org.studyeasy.springstrater.Model.Authority;
import org.studyeasy.springstrater.Repositories.AccountRepository;
import org.studyeasy.springstrater.util.constants.Role;

@Service
public class AccountService  implements UserDetailsService{
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public Account saveAccount(Account account) {
        if(account.getRole()==null){

            account.setRole(Role.USER.getRole());
        }
        // if(account.getPhoto()==null){
        //     String path="/images/img1.jpg";
        //     account.setPhoto(path);
        // }
        
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }   
    public Optional<Account> getAccountByEmail(String email) {
        return accountRepository.findOneByEmailIgnoreCase(email);
    }
    public Optional<Account> getAccountByToken(String token) {
        return accountRepository.findByResetPasswordToken(token);
    }
     public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }
    public void deleteAccountById(@NonNull Account account) {
        accountRepository.delete(account);
    }
   
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Account> accountOptional=accountRepository.findOneByEmailIgnoreCase(email);
        if(!accountOptional.isPresent()){
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        Account account=accountOptional.get();
        List<GrantedAuthority> authorities=new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(account.getRole()));
        for(Authority _auth:account.getAuthorities()){
             authorities.add(new SimpleGrantedAuthority(_auth.getName()));
        }
        return new User(account.getEmail(), account.getPassword(), authorities);
    }

   
}
