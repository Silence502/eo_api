package com.eoapi.app.Settings;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SettingsService {

	private final SettingsRepository settingsRepository;

	public SettingsService(SettingsRepository settingsRepository) {
		this.settingsRepository = settingsRepository;
	}

	public SettingsModel findByUserId(Long userId) {
		return settingsRepository.findByUserId(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Settings not found"));
	}

	public SettingsModel updateByUserId(Long userId, SettingsModel settings) {
		SettingsModel existing = settingsRepository.findByUserId(userId)
				.orElseGet(() -> {
					SettingsModel newSettings = new SettingsModel();
					newSettings.setUserId(userId);
					return newSettings;
				});

		existing.setTheme(settings.getTheme());
		existing.setLanguage(settings.getLanguage());
		existing.setTimezone(settings.getTimezone());
		existing.setNotificationsEmail(settings.getNotificationsEmail());
		existing.setNotificationsSms(settings.getNotificationsSms());
		existing.setUpdatedAt(LocalDateTime.now());

		return settingsRepository.save(existing);
	}
}
