package com.eoapi.app.Users;

import com.eoapi.app.common.Address;
import com.eoapi.app.common.Business;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class UserModel implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String email;

	@JsonIgnore
	@Column(nullable = false)
	private String password;

	@Convert(converter = RoleConverter.class)
	@Column(nullable = false)
	private Role role = Role.USER;

	private String plan = "free";
	private String firstName;
	private String lastName;

	@Embedded
	private Address address;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "name", column = @Column(name = "business_name")),
			@AttributeOverride(name = "vatNumber", column = @Column(name = "business_vat_number")),
			@AttributeOverride(name = "phone", column = @Column(name = "business_phone")),
			@AttributeOverride(name = "email", column = @Column(name = "business_email")),
			@AttributeOverride(name = "website", column = @Column(name = "business_website")),
			@AttributeOverride(name = "address.street", column = @Column(name = "business_street")),
			@AttributeOverride(name = "address.otherStreet", column = @Column(name = "business_other_street")),
			@AttributeOverride(name = "address.city", column = @Column(name = "business_city")),
			@AttributeOverride(name = "address.state", column = @Column(name = "business_state")),
			@AttributeOverride(name = "address.zip", column = @Column(name = "business_zip")),
			@AttributeOverride(name = "address.country", column = @Column(name = "business_country"))
	})
	private Business business;

	@JsonIgnore
	@Column(columnDefinition = "TEXT")
	private String hashedRefreshToken;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime createdAt;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
	}

	@Override
	public String getUsername() {
		return email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
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

	public Address getAddress() {
		if (address == null)
			address = new Address();
		return address;
	}

	public void setAddress(Address address) {
		if (this.address == null)
			this.address = new Address();
		this.address = address;
	}

	public Business getBusiness() {
		if (business == null)
			business = new Business();
		return business;
	}

	public void setBusiness(Business business) {
		if (this.business == null)
			this.business = new Business();
		this.business = business;
	}

	public String getHashedRefreshToken() {
		return hashedRefreshToken;
	}

	public void setHashedRefreshToken(String hashedRefreshToken) {
		this.hashedRefreshToken = hashedRefreshToken;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}
