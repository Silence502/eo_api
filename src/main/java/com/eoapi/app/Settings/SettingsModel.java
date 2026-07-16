package com.eoapi.app.Settings;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import java.time.LocalDateTime;

@Entity
@Table(name = "settings")
public class SettingsModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "theme", nullable = false)
	private String theme;

	@Column(name = "language", nullable = false)
	private String language;

	@Column(name = "timezone", nullable = false)
	private String timezone;

	@JsonProperty("notifications_email")
	@ColumnDefault("1")
	@Column(name = "notifications_email", nullable = false)
	private Boolean notificationsEmail;

	@JsonProperty("notifications_sms")
	@ColumnDefault("0")
	@Column(name = "notifications_sms", nullable = false)
	private Boolean notificationsSms;

	@Column(name = "updated_at")
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

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
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
