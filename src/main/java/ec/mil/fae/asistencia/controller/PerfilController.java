package ec.mil.fae.asistencia.controller;

import ec.mil.fae.asistencia.dto.request.UpdateEmailRequest;
import ec.mil.fae.asistencia.dto.request.UpdatePasswordRequest;
import ec.mil.fae.asistencia.dto.response.ApiResponse;
import ec.mil.fae.asistencia.dto.response.PerfilResponse;
import ec.mil.fae.asistencia.service.PerfilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/perfil")
@RequiredArgsConstructor
public class PerfilController {

    private final PerfilService perfilService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<PerfilResponse>> me(@AuthenticationPrincipal UserDetails userDetails) {
        PerfilResponse res = perfilService.me(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(res));
    }

    @PutMapping("/email")
    public ResponseEntity<ApiResponse<PerfilResponse>> updateEmail(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateEmailRequest req
    ) {
        PerfilResponse res = perfilService.updateCorreo(userDetails.getUsername(), req.getCorreo());
        return ResponseEntity.ok(ApiResponse.success("Correo actualizado correctamente", res));
    }

    @PutMapping("/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdatePasswordRequest req
    ) {
        perfilService.updatePassword(userDetails.getUsername(), req.getCurrentPassword(), req.getNewPassword());
        return ResponseEntity.ok(ApiResponse.success("Contraseña actualizada correctamente", null));
    }
}
