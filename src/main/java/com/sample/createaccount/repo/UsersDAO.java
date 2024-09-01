package com.sample.createaccount.repo;

import com.sample.createaccount.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersDAO extends JpaRepository<User,Long> {
    User findByEmailId(String emailId);
}
