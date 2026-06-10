package com.eoapi.app.Auth.dto;

import com.eoapi.app.common.dto.AddressRequest;
import com.eoapi.app.common.dto.BusinessRequest;

public class UpdateProfileRequest {

	private String email;
	private String firstName;
	private String lastName;
	private AddressRequest address;
	private BusinessRequest business;

	public BusinessRequest getBusiness() {
		return business;
	}

	public void setBusiness(BusinessRequest business) {
		this.business = business;
	}

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
