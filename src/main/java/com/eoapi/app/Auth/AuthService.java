package com.eoapi.app.Auth;

import com.eoapi.app.Auth.dto.AuthResponse;
import com.eoapi.app.Auth.dto.LoginRequest;
import com.eoapi.app.Auth.dto.ProfileResponse;
import com.eoapi.app.Auth.dto.RegisterRequest;
import com.eoapi.app.Consents.ConsentModel;
import com.eoapi.app.Consents.ConsentRepository;
import com.eoapi.app.Security.JwtService;
import com.eoapi.app.Users.UserModel;
import com.eoapi.app.Users.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final ConsentRepository consentRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public AuthService(UserRepository userRepository, ConsentRepository consentRepository,
			PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.userRepository = userRepository;
		this.consentRepository = consentRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	public AuthResponse register(RegisterRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
		}

		UserModel user = new UserModel();
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user = userRepository.save(user);

		saveConsent(user.getId(), "terms", request.getTermsAccepted(), request.getConsentVersion());
		saveConsent(user.getId(), "privacy", request.getPrivacyAccepted(), request.getConsentVersion());

		return buildTokens(user);
	}

	public AuthResponse login(LoginRequest request) {
		UserModel user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
		}

		return buildTokens(user);
	}

	public ProfileResponse profile(UserModel user) {
		return new ProfileResponse(
				user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(),
				user.getRole().name(), user.getPlan(), user.getCreatedAt(), user.getUpdatedAt());
	}

	public AuthResponse refresh(UserModel user, String rawRefreshToken) {
		if (user.getHashedRefreshToken() == null ||
				!sha256(rawRefreshToken).equals(user.getHashedRefreshToken())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
		}
		return buildTokens(user);
	}

	public void logout(UserModel user) {
		user.setHashedRefreshToken(null);
		userRepository.save(user);
	}

	private AuthResponse buildTokens(UserModel user) {
		String accessToken = jwtService.generateAccessToken(user);
		String refreshToken = jwtService.generateRefreshToken(user);
		user.setHashedRefreshToken(sha256(refreshToken));
		userRepository.save(user);
		return new AuthResponse(accessToken, refreshToken);
	}

	private String sha256(String input) {
		try {
			byte[] hash = MessageDigest.getInstance("SHA-256").digest(input.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(hash);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	private void saveConsent(Long userId, String type, Boolean given, String version) {
		ConsentModel consent = new ConsentModel();
		consent.setUserId(userId);
		consent.setType(type);
		consent.setGiven(given != null && given);
		consent.setVersion(version);
		consentRepository.save(consent);
	}
}
