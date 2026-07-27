package com.eoapi.app.Auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

	@NotBlank
	@Email
	private String email;

	@NotBlank
	@Size(min = 8)
	private String password;

	private String firstName;
	private String lastName;

	@NotNull
	private Boolean termsAccepted;

	@NotNull
	private Boolean privacyAccepted;

	@NotBlank
	private String consentVersion;

	private Boolean marketingAccepted;

	private String language;

	private String theme;

	private String timezone;

	private Boolean notificationsEmail;

	private Boolean notificationsSms;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Boolean getTermsAccepted() {
		return termsAccepted;
	}

	public void setTermsAccepted(Boolean termsAccepted) {
		this.termsAccepted = termsAccepted;
	}

	public Boolean getPrivacyAccepted() {
		return privacyAccepted;
	}

	public void setPrivacyAccepted(Boolean privacyAccepted) {
		this.privacyAccepted = privacyAccepted;
	}

	public String getConsentVersion() {
		return consentVersion;
	}

	public Boolean getMarketingAccepted() {
		return marketingAccepted;
	}

	public void setMarketingAccepted(Boolean marketingAccepted) {
		this.marketingAccepted = marketingAccepted;
	}

	public void setConsentVersion(String consentVersion) {
		this.consentVersion = consentVersion;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
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
}
