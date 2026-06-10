package com.eoapi.app.Auth;

import com.eoapi.app.Auth.dto.AuthResponse;
import com.eoapi.app.Auth.dto.LoginRequest;
import com.eoapi.app.Auth.dto.ProfileResponse;
import com.eoapi.app.Auth.dto.RegisterRequest;
import com.eoapi.app.Auth.dto.UpdateProfileRequest;
import com.eoapi.app.Users.UserModel;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1.0/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
		return ResponseEntity.ok(authService.login(request));
	}

	@GetMapping("/profile")
	public ResponseEntity<ProfileResponse> profile(@AuthenticationPrincipal UserModel user) {
		return ResponseEntity.ok(authService.profile(user));
	}

	@PatchMapping("/profile")
	public ResponseEntity<ProfileResponse> updateProfile(@AuthenticationPrincipal UserModel user,
			@RequestBody UpdateProfileRequest request) {
		return ResponseEntity.ok(authService.updateProfile(user, request));
	}

	@PostMapping("/refresh")
	public ResponseEntity<AuthResponse> refresh(@AuthenticationPrincipal UserModel user,
			@RequestHeader("Authorization") String authHeader) {
		String rawToken = authHeader.substring(7);
		return ResponseEntity.ok(authService.refresh(user, rawToken));
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@AuthenticationPrincipal UserModel user) {
		authService.logout(user);
		return ResponseEntity.noContent().build();
	}
}
