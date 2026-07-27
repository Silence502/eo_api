package com.eoapi.app.Settings;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import java.time.LocalDateTime;

@Entity
@Table(name = "settings")
public class SettingsModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@Column(name = "timezone", nullable = false)
	private String timezone;

	@ColumnDefault("1")
	@Column(nullable = false)
	private Boolean notificationsEmail;

	@ColumnDefault("0")
	@Column(nullable = false)
	private Boolean notificationsSms;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime updatedAt;

	// Getters and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public Boolean getNotificationsEmail() {
		return notificationsEmail;
	}

	public void setNotificationsEmail(Boolean notificationsEmail) {
		this.notificationsEmail = notificationsEmail;
	}

	public Boolean getNotificationsSms() {
		return notificationsSms;
	}

	public void setNotificationsSms(Boolean notificationsSms) {
		this.notificationsSms = notificationsSms;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
