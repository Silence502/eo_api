package com.eoapi.app.Auth.dto;

import com.eoapi.app.common.Address;
import com.eoapi.app.common.Business;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class ProfileResponse {

	private Long id;
	private String email;
	private String firstName;
	private String lastName;
	private String phone;
	private String avatarUrl;
	private String role;
	private String plan;
	private Address address;
	private Business business;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime createdAt;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime updatedAt;

	public ProfileResponse(Long id, String email, String firstName, String lastName, String phone,
			String avatarUrl, String role, String plan, Address address, Business business,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.avatarUrl = avatarUrl;
		this.role = role;
		this.plan = plan;
		this.address = address;
		this.business = business;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhone() {
		return phone;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public String getRole() {
		return role;
	}

	public String getPlan() {
		return plan;
	}

	public Address getAddress() {
		return address;
	}

	public Business getBusiness() {
		return business;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}
