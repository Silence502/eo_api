package com.eoapi.app.Events.dto;

import jakarta.validation.constraints.NotBlank;

public class LocationRequest {

	@NotBlank
	private String name;

	@NotBlank
	private String street;

	private String otherStreet;

	@NotBlank
	private String city;

	private String state;

	@NotBlank
	private String country;

	@NotBlank
	private String zipCode;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getOtherStreet() {
		return otherStreet;
	}

	public void setOtherStreet(String otherStreet) {
		this.otherStreet = otherStreet;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
}
