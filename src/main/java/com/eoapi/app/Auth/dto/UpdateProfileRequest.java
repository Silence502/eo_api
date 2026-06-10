package com.eoapi.app.Auth.dto;

import com.eoapi.app.common.dto.AddressRequest;

public class UpdateProfileRequest {

	private String email;
	private String firstName;
	private String lastName;
	private AddressRequest address;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public AddressRequest getAddress() {
		return address;
	}

	public void setAddress(AddressRequest address) {
		this.address = address;
	}
}
