package com.technest_api.module.user;

import com.technest_api.common.constant.enums.Role;
import com.technest_api.module.user.model.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByGoogleId(String googleId);

    Boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u set u.role = :newRole where u.id = :userId AND u.role = :currentRole")
    void updateRoleIfCurrent(@Param("id") UUID userId, @Param("newRole") Role newRole,
                             @Param("currentRole") Role currentRole);
}
