package org.studyeasy.springstrater.Config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.studyeasy.springstrater.Model.Account;
import org.studyeasy.springstrater.Model.Authority;
import org.studyeasy.springstrater.Model.Post;
import org.studyeasy.springstrater.Repositories.PostRepository;
import org.studyeasy.springstrater.Services.AccountService;
import org.studyeasy.springstrater.Services.AuthorityService;
import org.studyeasy.springstrater.util.constants.Privileges;
import org.studyeasy.springstrater.util.constants.Role;

@Component
public class DataSeeding implements CommandLineRunner {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorityService authorityService;

    @Override
    public void run(String... args) {

        // =========================
        // 1. Seed Authorities
        // =========================
        for (Privileges privilege : Privileges.values()) {
            authorityService.findById(privilege.getId())
                    .orElseGet(() -> {
                        Authority authority = new Authority();
                        authority.setId(privilege.getId());
                        authority.setName(privilege.getName());
                        return authorityService.save(authority);
                    });
        }

        // =========================
        // 2. Seed Accounts
        // =========================
        if (accountService.getAccountByEmail("john.doe@gmail.com").isEmpty()) {

            Account account1 = new Account();
            account1.setFirstName("John");
            account1.setLastName("Doe");
            account1.setEmail("john.doe@gmail.com");
            account1.setPassword("password123");
            account1.setRole(Role.USER.getRole());
            account1.setGender("Male");
            account1.setAge(25);
            account1.setDate_of_birth(LocalDate.of(1999, 5, 15));
            accountService.saveAccount(account1);

            Account account2 = new Account();
            account2.setFirstName("Jane");
            account2.setLastName("Smith");
            account2.setEmail("jane.smith@gmail.com");
            account2.setPassword("password456");
            account2.setRole(Role.ADMIN.getRole());
            account2.setGender("Female");
            account2.setAge(30);
            account2.setDate_of_birth(LocalDate.of(1994, 3, 10));
            accountService.saveAccount(account2);

            Account account3 = new Account();
            account3.setFirstName("Alice");
            account3.setLastName("Johnson");
            account3.setEmail("editor@gmail.com");
            account3.setPassword("password789");
            account3.setRole(Role.EDITOR.getRole());
            account3.setGender("Female");
            account3.setAge(28);
            account3.setDate_of_birth(LocalDate.of(1996, 8, 20));
            accountService.saveAccount(account3);

            Account account4 = new Account();
            account4.setFirstName("Bob");
            account4.setLastName("Brown");
            account4.setEmail("super_editor@gmail.com");
            account4.setPassword("password012");
            account4.setRole(Role.EDITOR.getRole());
            account4.setGender("Male");
            account4.setAge(35);
            account4.setDate_of_birth(LocalDate.of(1989, 1, 5));

            Set<Authority> authorities = new HashSet<>();
            authorityService.findById(Privileges.RESET_PASSWORD.getId()).ifPresent(authorities::add);
            authorityService.findById(Privileges.ACCESS_ADMIN_PANEL.getId()).ifPresent(authorities::add);

            account4.setAuthorities(authorities);
            accountService.saveAccount(account4);
        }

        // =========================
        // 3. Seed Posts (20 posts)
        // =========================
        if (postRepository.count() == 0) {

            Account account1 = accountService.getAccountByEmail("john.doe@gmail.com").orElseThrow();
            Account account2 = accountService.getAccountByEmail("jane.smith@gmail.com").orElseThrow();

            for (int i = 1; i <= 20; i++) {

                Post post = new Post();
                post.setTitle("Post Number " + i);

                String body = """
                        This is line one of post %d.
                        This is line two with more details.
                        This is line three explaining something important.
                        This is line four concluding the post.
                        """.formatted(i);

                post.setBody(body);
                post.setCreatedAt(LocalDateTime.now().minusDays(20 - i));

                // Alternate between authors
                if (i % 2 == 0) {
                    post.setAccount(account1);
                } else {
                    post.setAccount(account2);
                }

                postRepository.save(post);
            }
        }
    }
}