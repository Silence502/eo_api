package com.eoapi.app.Events;

import jakarta.persistence.*;

@Entity
@Table(name = "event_user")
@IdClass(EventUserId.class)
public class EventUserModel {

	@Id
	@Column(name = "event_id")
	private Long eventId;

	@Id
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "is_owner")
	private Boolean isOwner = false;

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Boolean getIsOwner() {
		return isOwner;
	}

	public void setIsOwner(Boolean isOwner) {
		this.isOwner = isOwner;
	}
}
