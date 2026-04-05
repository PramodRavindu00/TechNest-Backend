package com.technest_api.module.auth;

import com.technest_api.common.annotation.SetRefreshTokenCookie;
import com.technest_api.module.auth.dto.AuthCodeExchangeRequest;
import com.technest_api.module.auth.dto.AuthTokens;
import com.technest_api.module.auth.dto.LoginRequest;
import com.technest_api.module.auth.dto.SignUpRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void localSignUp(@Valid @RequestBody SignUpRequest request) {
        authService.localSignUp(request);
    }

    @PostMapping("/login")
    @SetRefreshTokenCookie
    public ResponseEntity<AuthTokens> localLogin(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.localLogin(request));
    }

    @PostMapping("/refresh")
    @SetRefreshTokenCookie
    public ResponseEntity<AuthTokens> refresh(HttpServletRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    @PostMapping("/exchange")
    @SetRefreshTokenCookie
    public ResponseEntity<AuthTokens> exchange(
            @Valid @RequestBody AuthCodeExchangeRequest request) {
        return ResponseEntity.ok(authService.exchange(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok()
                .build();
    }


}
