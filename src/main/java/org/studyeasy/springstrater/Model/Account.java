package org.studyeasy.springstrater.Model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String firstName;
    private String lastName;
    @Email(message = "Invalid Email")
    @NotBlank(message = "Missing Email")
    private String email;
    @NotBlank(message = "Missing Password")
    private String password;
    private String role;
    private String gender;
    @Min(value=18)
    @Max(value=99)
    private int age;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date_of_birth;
    private String photo;
    private String resetPasswordToken;
    private LocalDateTime resetPasswordTokenExpiry;
    @OneToMany(mappedBy = "account")
    private List<Post> posts;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "account_authority", joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private Set<Authority> authorities = new HashSet<>();
}
