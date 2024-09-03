package com.sample.createaccount.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidUser() {
        User user = new User();
        user.setFullName("John Doe");
        user.setEmailId("john.doe@example.com");
        user.setPassword("Password123");
        user.setIsTermsAndConditionsAgreed(true);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "User should be valid");
    }

    @Test
    void testInvalidEmail() {
        User user = new User();
        user.setFullName("John Doe");
        user.setEmailId("invalid-email");
        user.setPassword("Password123");
        user.setIsTermsAndConditionsAgreed(true);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Invalid email should trigger a violation");

        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Email is not valid", violation.getMessage());
    }

    @Test
    void testBlankFullName() {
        User user = new User();
        user.setFullName("");
        user.setEmailId("john.doe@example.com");
        user.setPassword("Password123");
        user.setIsTermsAndConditionsAgreed(true);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(2, violations.size(), "Blank fullName should trigger a violation");

        ConstraintViolation<User> violation = violations.iterator().next();
        // Extract messages
        String actualMessage1 = violations.stream()
                .filter(v -> v.getMessage().contains("Name cannot be blank"))
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("Message not found");

        String actualMessage2 = violations.stream()
                .filter(v -> v.getMessage().contains("size must be between 3 and 50"))
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("Message not found");
        assertEquals("Name cannot be blank", actualMessage1);
        assertEquals("size must be between 3 and 50", actualMessage2);

    }

    @Test
    void testPasswordTooShort() {
        User user = new User();
        user.setFullName("John Doe");
        user.setEmailId("john.doe@example.com");
        user.setPassword("123");
        user.setIsTermsAndConditionsAgreed(true);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Short password should trigger a violation");

        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Password must be at least 8 characters long", violation.getMessage());
    }

    @Test
    void testInvalidPasswordPattern() {
        User user = new User();
        user.setFullName("John Doe");
        user.setEmailId("john.doe@example.com");
        user.setPassword("Pass@word");
        user.setIsTermsAndConditionsAgreed(true);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Password with invalid characters should trigger a violation");

        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Password must contain only alphanumeric characters", violation.getMessage());
    }

    @Test
    void testEmailNotEmpty() {
        User user = new User();
        user.setFullName("John Doe");
        user.setEmailId("");
        user.setPassword("Password123");
        user.setIsTermsAndConditionsAgreed(true);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(2, violations.size(), "Empty email should trigger a violation");

        ConstraintViolation<User> violation = violations.iterator().next();
// Extract messages
        String actualMessage1 = violations.stream()
                .filter(v -> v.getMessage().contains("Email is not valid"))
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("Message not found");

        String actualMessage2 = violations.stream()
                .filter(v -> v.getMessage().contains("Email cannot be empty"))
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("Message not found");
        assertEquals("Email is not valid", actualMessage1);
        assertEquals("Email cannot be empty", actualMessage2);    }
}
