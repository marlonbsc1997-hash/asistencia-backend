package ec.mil.fae.asistencia.controller;

import ec.mil.fae.asistencia.dto.request.LoginRequest;
import ec.mil.fae.asistencia.dto.response.ApiResponse;
import ec.mil.fae.asistencia.dto.response.LoginResponse;
import ec.mil.fae.asistencia.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse resp = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login exitoso", resp));
    }
}



