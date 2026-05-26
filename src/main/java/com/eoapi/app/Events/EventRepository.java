package com.eoapi.app.Events;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends CrudRepository<EventModel, Long> {

	@Query(value = "SELECT e.* FROM events e INNER JOIN event_user eu ON e.id = eu.event_id WHERE eu.user_id = :userId", nativeQuery = true)
	List<EventModel> findAllByUserId(@Param("userId") Long userId);
}
