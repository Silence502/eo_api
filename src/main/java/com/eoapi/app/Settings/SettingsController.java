package com.eoapi.app.Settings;

import com.eoapi.app.Users.UserModel;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1.0/settings")
public class SettingsController {

	private final SettingsService settingsService;

	public SettingsController(SettingsService settingsService) {
		this.settingsService = settingsService;
	}

	@GetMapping
	public ResponseEntity<SettingsModel> getByUserId(@AuthenticationPrincipal UserModel user) {
		return ResponseEntity.ok(settingsService.findByUserId(user.getId()));
	}

	@PatchMapping
	public ResponseEntity<SettingsModel> update(@Valid @RequestBody SettingsModel settings,
			@AuthenticationPrincipal UserModel user) {
		return ResponseEntity.ok(settingsService.updateByUserId(user.getId(), settings));
	}
}
