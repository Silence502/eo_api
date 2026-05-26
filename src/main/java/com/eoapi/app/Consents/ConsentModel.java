package com.eoapi.app.Consents;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consents")
public class ConsentModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(nullable = false)
	private String type;

	@Column(nullable = false)
	private String version;

	@Column(nullable = false)
	private boolean given;

	private LocalDateTime givenAt;

	@PrePersist
	protected void onCreate() {
		givenAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isGiven() {
		return given;
	}

	public void setGiven(boolean given) {
		this.given = given;
	}

	public LocalDateTime getGivenAt() {
		return givenAt;
	}
}
