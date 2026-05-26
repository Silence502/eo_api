package com.eoapi.app.Events;

import java.io.Serializable;
import java.util.Objects;

public class EventUserId implements Serializable {

	private Long eventId;
	private Long userId;

	public EventUserId() {
	}

	public EventUserId(Long eventId, Long userId) {
		this.eventId = eventId;
		this.userId = userId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof EventUserId that))
			return false;
		return Objects.equals(eventId, that.eventId) && Objects.equals(userId, that.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(eventId, userId);
	}

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
}
