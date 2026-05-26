package com.eoapi.app.Events;

import org.springframework.data.repository.CrudRepository;

public interface EventUserRepository extends CrudRepository<EventUserModel, EventUserId> {

	boolean existsByEventIdAndUserIdAndIsOwner(Long eventId, Long userId, Boolean isOwner);

	void deleteByEventId(Long eventId);
}
