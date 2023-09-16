package com.example.propertymanagement.repository;


import com.example.propertymanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    List<User> findUsersByPropertiesId(Long propertyId);
    @Query("SELECT s FROM User s WHERE SUBSTRING(s.email, 1, 1) = :letter")
    List<User> findUsersByEmailStartingWith(@Param("letter") String letter);
}
