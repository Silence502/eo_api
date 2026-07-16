package com.eoapi.app.Settings;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SettingsRepository extends CrudRepository<SettingsModel, Long> {
	Optional<SettingsModel> findByUserId(Long userId);
}
