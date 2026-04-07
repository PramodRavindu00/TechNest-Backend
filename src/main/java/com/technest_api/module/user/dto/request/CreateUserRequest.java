package com.technest_api.module.user.dto.request;

import com.technest_api.module.auth.dto.request.SignUpRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateUserRequest {

    @NotBlank()
    @Email()
    private String email;

    @NotBlank()
    @Size(min = 8)
    private String password;

    public CreateUserRequest(SignUpRequest dto) {
        this.email = dto.getEmail();
        this.password = dto.getPassword();
    }
}
