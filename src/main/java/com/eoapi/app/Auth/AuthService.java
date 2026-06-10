package com.eoapi.app.Auth;

import com.eoapi.app.Auth.dto.AuthResponse;
import com.eoapi.app.Auth.dto.LoginRequest;
import com.eoapi.app.Auth.dto.ProfileResponse;
import com.eoapi.app.Auth.dto.RegisterRequest;
import com.eoapi.app.Auth.dto.UpdateProfileRequest;
import com.eoapi.app.common.Address;
import com.eoapi.app.Consents.ConsentModel;
import com.eoapi.app.Settings.SettingsModel;
import com.eoapi.app.Settings.SettingsRepository;
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
	private final SettingsRepository settingsRepository;

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
				user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(),
				user.getRole().name(), user.getPlan(), user.getAddress(), user.getCreatedAt(), user.getUpdatedAt());
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
		userRepository.save(user);
		return profile(user);
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

	private void saveSettings(Long userId, RegisterRequest request) {
		SettingsModel languageSetting = new SettingsModel();
		languageSetting.setUserId(userId);
		languageSetting.setKey("language");
		languageSetting.setValue(request.getLanguage() != null ? request.getLanguage() : "en");
		settingsRepository.save(languageSetting);

		SettingsModel themeSetting = new SettingsModel();
		themeSetting.setUserId(userId);
		themeSetting.setKey("theme");
		themeSetting.setValue(request.getTheme() != null ? request.getTheme() : "light");
		settingsRepository.save(themeSetting);

		SettingsModel timezoneSetting = new SettingsModel();
		timezoneSetting.setUserId(userId);
		timezoneSetting.setKey("timezone");
		timezoneSetting.setValue(request.getTimezone() != null ? request.getTimezone() : "UTC");
		settingsRepository.save(timezoneSetting);

		SettingsModel receiveNotificationsSetting = new SettingsModel();
		receiveNotificationsSetting.setUserId(userId);
		receiveNotificationsSetting.setKey("receive_notifications");
		receiveNotificationsSetting
				.setValue(request.getReceiveNotifications() != null ? request.getReceiveNotifications() : "true");
		settingsRepository.save(receiveNotificationsSetting);

		SettingsModel receiveRemindersSetting = new SettingsModel();
		receiveRemindersSetting.setUserId(userId);
		receiveRemindersSetting.setKey("receive_reminders");
		receiveRemindersSetting
				.setValue(request.getRevceiveReminders() != null ? request.getRevceiveReminders() : "true");
		settingsRepository.save(receiveRemindersSetting);
	}
}
