package com.sample.createaccount.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USER_TBL")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 3, max = 100)
    @NotBlank(message = "Username cannot be blank")
    private String fullName;

    @Email(message = "Email is not valid", regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", flags = Pattern.Flag.CASE_INSENSITIVE)
    @NotEmpty(message = "Email cannot be empty")
    private String emailId;

    @Transient
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Password must contain only alphanumeric characters")
    private String password;

    private String encodedPassword;

    private Boolean isTermsAndConditionsAgreed = false;

}
