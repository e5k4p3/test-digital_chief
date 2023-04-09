package com.e5k4p3.digitalchief.user.dao;

import com.e5k4p3.digitalchief.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
}
