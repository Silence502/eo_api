package com.eoapi.app.Auth;

import com.eoapi.app.Auth.dto.AuthResponse;
import com.eoapi.app.Auth.dto.LoginRequest;
import com.eoapi.app.Auth.dto.ProfileResponse;
import com.eoapi.app.Auth.dto.RegisterRequest;
import com.eoapi.app.Auth.dto.UpdateProfileRequest;
import com.eoapi.app.common.Address;
import com.eoapi.app.common.Business;
import com.eoapi.app.Consents.ConsentModel;
import com.eoapi.app.Settings.SettingsModel;
import com.eoapi.app.Settings.SettingsRepository;
import com.eoapi.app.Consents.ConsentRepository;
import com.eoapi.app.Security.JwtService;
import com.eoapi.app.Users.UserModel;
import com.eoapi.app.Users.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

	private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
			"image/jpeg", "image/png", "image/gif", "image/webp");

	private final UserRepository userRepository;
	private final ConsentRepository consentRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final SettingsRepository settingsRepository;

	@Value("${upload.avatar-dir:uploads/avatars}")
	private String avatarDir;

	public AuthService(UserRepository userRepository, ConsentRepository consentRepository,
			PasswordEncoder passwordEncoder, JwtService jwtService, SettingsRepository settingsRepository) {
		this.userRepository = userRepository;
		this.consentRepository = consentRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.settingsRepository = settingsRepository;
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
		saveConsent(user.getId(), "marketing", request.getMarketingAccepted(), request.getConsentVersion());

		saveSettings(user.getId(), request);

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
				user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getPhone(),
				user.getAvatarUrl(), user.getRole().name(), user.getStatus().name(), user.getPlan(), user.getAddress(),
				user.getBusiness(),
				user.getCreatedAt(), user.getUpdatedAt());
	}

	public ProfileResponse updateProfile(UserModel user, UpdateProfileRequest request) {
		if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
			if (userRepository.existsByEmail(request.getEmail())) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
			}
			user.setEmail(request.getEmail());
		}
		if (request.getFirstName() != null)
			user.setFirstName(request.getFirstName());
		if (request.getLastName() != null)
			user.setLastName(request.getLastName());
		if (request.getPhone() != null)
			user.setPhone(request.getPhone());
		if (request.getAddress() != null) {
			Address address = user.getAddress() != null ? user.getAddress() : new Address();
			if (request.getAddress().getStreet() != null)
				address.setStreet(request.getAddress().getStreet());
			if (request.getAddress().getOtherStreet() != null)
				address.setOtherStreet(request.getAddress().getOtherStreet());
			if (request.getAddress().getCity() != null)
				address.setCity(request.getAddress().getCity());
			if (request.getAddress().getState() != null)
				address.setState(request.getAddress().getState());
			if (request.getAddress().getZip() != null)
				address.setZip(request.getAddress().getZip());
			if (request.getAddress().getCountry() != null)
				address.setCountry(request.getAddress().getCountry());
			user.setAddress(address);
		}
		if (request.getBusiness() != null) {
			Business business = user.getBusiness() != null ? user.getBusiness() : new Business();
			if (request.getBusiness().getName() != null)
				business.setName(request.getBusiness().getName());
			if (request.getBusiness().getVatNumber() != null)
				business.setVatNumber(request.getBusiness().getVatNumber());
			if (request.getBusiness().getPhone() != null)
				business.setPhone(request.getBusiness().getPhone());
			if (request.getBusiness().getEmail() != null)
				business.setEmail(request.getBusiness().getEmail());
			if (request.getBusiness().getWebsite() != null)
				business.setWebsite(request.getBusiness().getWebsite());
			if (request.getBusiness().getAddress() != null) {
				Address businessAddress = business.getAddress() != null ? business.getAddress() : new Address();
				if (request.getBusiness().getAddress().getStreet() != null)
					businessAddress.setStreet(request.getBusiness().getAddress().getStreet());
				if (request.getBusiness().getAddress().getOtherStreet() != null)
					businessAddress.setOtherStreet(request.getBusiness().getAddress().getOtherStreet());
				if (request.getBusiness().getAddress().getCity() != null)
					businessAddress.setCity(request.getBusiness().getAddress().getCity());
				if (request.getBusiness().getAddress().getState() != null)
					businessAddress.setState(request.getBusiness().getAddress().getState());
				if (request.getBusiness().getAddress().getZip() != null)
					businessAddress.setZip(request.getBusiness().getAddress().getZip());
				if (request.getBusiness().getAddress().getCountry() != null)
					businessAddress.setCountry(request.getBusiness().getAddress().getCountry());
				business.setAddress(businessAddress);
			}
			user.setBusiness(business);
		}
		userRepository.save(user);
		return profile(user);
	}

	public ProfileResponse uploadAvatar(UserModel user, MultipartFile file) {
		String contentType = file.getContentType();
		if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
			throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
					"Only JPEG, PNG, GIF and WebP images are allowed");
		}
		if (file.getSize() > 5 * 1024 * 1024) {
			throw new ResponseStatusException(HttpStatus.CONTENT_TOO_LARGE, "Avatar must be under 5 MB");
		}

		String extension = contentType.substring(contentType.lastIndexOf('/') + 1).replace("jpeg", "jpg");
		String filename = UUID.randomUUID() + "." + extension;

		try {
			Path uploadPath = Paths.get(avatarDir).toAbsolutePath().normalize();
			Files.createDirectories(uploadPath);

			// Delete old avatar file if present
			if (user.getAvatarUrl() != null) {
				Path old = Paths.get(user.getAvatarUrl().replaceFirst("^/", "")).toAbsolutePath().normalize();
				if (old.startsWith(uploadPath)) {
					Files.deleteIfExists(old);
				}
			}

			Path target = uploadPath.resolve(filename);
			Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store avatar");
		}

		user.setAvatarUrl("/uploads/avatars/" + filename);
		userRepository.save(user);
		return profile(user);
	}

	public AuthResponse refresh(String rawRefreshToken) {
		String email;
		try {
			email = jwtService.extractUsername(rawRefreshToken);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
		}
		UserModel user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));
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

	private void saveSettings(Long userId, RegisterRequest request) {
		SettingsModel settings = new SettingsModel();
		settings.setUserId(userId);
		settings.setTimezone(request.getTimezone() != null ? request.getTimezone() : "UTC");
		settings.setNotificationsEmail(
				request.getNotificationsEmail() != null ? request.getNotificationsEmail() : true);
		settings.setNotificationsSms(request.getNotificationsSms() != null ? request.getNotificationsSms() : false);
		settingsRepository.save(settings);
	}
}