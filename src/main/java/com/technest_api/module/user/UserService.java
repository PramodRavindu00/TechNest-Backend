package com.technest_api.module.user;

import com.technest_api.common.constant.enums.Role;
import com.technest_api.common.exception.OAuth2AuthenticationException;
import com.technest_api.common.security.AuthenticatedUser;
import com.technest_api.module.user.dto.CreateUserDto;
import com.technest_api.module.user.dto.UserResponseDto;
import com.technest_api.module.user.mapper.UserMapper;
import com.technest_api.module.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public List<UserResponseDto> getAll() {
        List<User> users = userRepo.findAll();
        return userMapper.toDtoList(users);
    }

    public UserResponseDto getOne(String id) {
        User user = userRepo.findById(UUID.fromString(id))
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return userMapper.toDto(user);
    }

    public void createAdminUserIfNotExists() {
        if (userRepo.existsByEmail("admin@technest.com")) {
            log.info("Admin already exists - skipping seed");
            return;
        }

        User admin = User.builder()
                .email("admin@technest.com")
                .passwordHash(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .emailVerified(true)
                .build();
        userRepo.save(admin);
        log.info("Admin user seeded successfully");
    }

    public void createUser(CreateUserDto request) {
        User newUser = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepo.save(newUser);
    }

    public Optional<User> findById(String id) {
        return userRepo.findById(UUID.fromString(id));
    }

    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    // this method used in Oauth2 authentication
    public User findOrCreate(String googleId, String email) {
        Optional<User> byGoogleId = userRepo.findByGoogleId(googleId);
        if (byGoogleId.isPresent()) {
            return byGoogleId.get();  // return the user found from googleId
        }
        //not found by googleId? check email exists
        Optional<User> byEmail = userRepo.findByEmail(email);
        if (byEmail.isPresent()) {
            throw OAuth2AuthenticationException.accountConflict(email);
        }

        User newUser = User.builder()
                .email(email)
                .googleId(googleId)
                .build();
        return userRepo.save(newUser);
    }

    @Transactional
    public void promoteToAuthorIfReader(AuthenticatedUser user) {
        userRepo.updateRoleIfCurrent(user.getId(), Role.AUTHOR, Role.READER);
    }
}
