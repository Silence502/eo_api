package com.eoapi.app.Events;

import com.eoapi.app.Events.dto.EventRequest;
import com.eoapi.app.Users.UserModel;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1.0/events")
public class EventController {

	private final EventService eventService;

	public EventController(EventService eventService) {
		this.eventService = eventService;
	}

	@GetMapping
	public ResponseEntity<List<EventModel>> getAll(@AuthenticationPrincipal UserModel user) {
		return ResponseEntity.ok(eventService.findAll(user));
	}

	@GetMapping("/{id}")
	public ResponseEntity<EventModel> getById(@PathVariable Long id, @AuthenticationPrincipal UserModel user) {
		return ResponseEntity.ok(eventService.findById(id, user));
	}

	@PostMapping
	public ResponseEntity<EventModel> create(@Valid @RequestBody EventRequest request,
			@AuthenticationPrincipal UserModel user) {
		return ResponseEntity.status(HttpStatus.CREATED).body(eventService.create(request, user));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EventModel> update(@PathVariable Long id,
			@Valid @RequestBody EventRequest request,
			@AuthenticationPrincipal UserModel user) {
		return ResponseEntity.ok(eventService.update(id, request, user));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserModel user) {
		eventService.delete(id, user);
		return ResponseEntity.noContent().build();
	}
}
