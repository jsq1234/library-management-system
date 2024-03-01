package library.backend.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import library.backend.api.dto.AuthResponseDto;
import library.backend.api.dto.LoginByEmailDto;
import library.backend.api.dto.LoginByPhoneNoDto;
import library.backend.api.dto.SignUpRequestDto;
import library.backend.api.services.AuthService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login/email")
    public ResponseEntity<AuthResponseDto> loginUserByEmail(
            @Valid @RequestBody LoginByEmailDto dto) {

        AuthResponseDto response = authService.loginByEmail(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/login/phone")
    public ResponseEntity<AuthResponseDto> loginUserByPhoneNo(
            @Valid @RequestBody LoginByPhoneNoDto dto) {

        AuthResponseDto response = authService.loginByPhoneNo(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> signUp(
            @Valid @RequestBody SignUpRequestDto signUpRequestDto) {

        AuthResponseDto response = authService.signUp(signUpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
