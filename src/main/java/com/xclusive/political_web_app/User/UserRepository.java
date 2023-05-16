package com.xclusive.political_web_app.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    @Query(value = "SELECT password FROM app_user WHERE email = :email", nativeQuery = true)
    String checkUserPasswordByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM app_user WHERE email = :email", nativeQuery = true)
    AppUser getUserDetailsByEmail(@Param("email") String email);

    Optional<AppUser> findByEmail(String email);
}
