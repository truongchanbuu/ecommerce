package com.buutruong.ecommerce.features.auth.repository;

import com.buutruong.ecommerce.features.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.enabled = :enabled WHERE u.email = :email")
    void enableUser(@Param("email") String email, @Param("enabled") boolean enabled);

    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.email = :email")
    void updatePassword(@Param("email") String email, @Param("password") String password);

    @Modifying
    @Query("UPDATE User u SET u.email = :newEmail WHERE u.email = :oldEmail")
    int updateEmail(@Param("oldEmail") String oldEmail, @Param("newEmail") String newEmail);

    @Modifying
    @Query("UPDATE User u SET u.enabled = false")
    int disabledUser(String email);

    @Modifying
    void deleteByEmail(String email);
}
