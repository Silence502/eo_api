package com.eoapi.app.Events;

import com.eoapi.app.Companies.CompanyModel;
import com.eoapi.app.Companies.CompanyRepository;
import com.eoapi.app.Events.dto.EventRequest;
import com.eoapi.app.Users.UserModel;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EventService {

	private final EventRepository eventRepository;
	private final LocationRepository locationRepository;
	private final EventUserRepository eventUserRepository;
	private final CompanyRepository companyRepository;

	public EventService(EventRepository eventRepository, LocationRepository locationRepository,
			EventUserRepository eventUserRepository, CompanyRepository companyRepository) {
		this.eventRepository = eventRepository;
		this.locationRepository = locationRepository;
		this.eventUserRepository = eventUserRepository;
		this.companyRepository = companyRepository;
	}

	public List<EventModel> findAll(UserModel user) {
		return eventRepository.findAllByUserId(user.getId());
	}

	public EventModel findById(Long id, UserModel user) {
		EventModel event = eventRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
		assertOwner(id, user);
		return event;
	}

	@Transactional
	public EventModel create(EventRequest request, UserModel user) {
		EventModel event = mapToModel(new EventModel(), request);
		event = eventRepository.save(event);

		if (request.getLocation() != null) {
			LocationModel location = buildLocation(request, event.getId());
			location = locationRepository.save(location);
			event.setLocation(location);
			event = eventRepository.save(event);
		}

		if (request.getCompanyId() != null) {
			event.setCompanyId(request.getCompanyId());
		} else {
			List<CompanyModel> companies = (List<CompanyModel>) companyRepository.findAll();
			if (companies.size() == 1) {
				event.setCompanyId(companies.get(0).getId());
			} else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"companyId is required when multiple companies exist");
			}
		}
		event = eventRepository.save(event);

		EventUserModel eventUser = new EventUserModel();
		eventUser.setEventId(event.getId());
		eventUser.setUserId(user.getId());
		eventUser.setIsOwner(true);
		eventUserRepository.save(eventUser);

		return event;
	}

	@Transactional
	public EventModel update(Long id, EventRequest request, UserModel user) {
		EventModel event = findById(id, user);
		mapToModel(event, request);

		if (request.getLocation() != null) {
			LocationModel location = event.getLocation();
			if (location == null) {
				location = buildLocation(request, id);
			} else {
				applyLocationRequest(location, request);
			}
			location = locationRepository.save(location);
			event.setLocation(location);
		}

		return eventRepository.save(event);
	}

	@Transactional
	public void delete(Long id, UserModel user) {
		assertOwner(id, user);
		EventModel event = eventRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
		eventUserRepository.deleteByEventId(id);
		if (event.getLocation() != null) {
			locationRepository.deleteById(event.getLocation().getId());
		}
		eventRepository.deleteById(id);
	}

	private void assertOwner(Long eventId, UserModel user) {
		if (!eventUserRepository.existsByEventIdAndUserIdAndIsOwner(eventId, user.getId(), true)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
		}
	}

	private EventModel mapToModel(EventModel event, EventRequest request) {
		event.setName(request.getName());
		event.setDescription(request.getDescription());
		event.setType(request.getType());
		event.setNbGuests(request.getNbGuests());
		event.setStartDate(request.getStartDate());
		event.setEndDate(request.getEndDate());
		event.setCurrencyCode(request.getCurrencyCode());
		event.setDurationType(request.getDurationType());
		event.setDuration(request.getDuration());
		event.setPrice(request.getPrice());
		event.setCompanyId(request.getCompanyId());
		return event;
	}

	private LocationModel buildLocation(EventRequest request, Long eventId) {
		LocationModel location = new LocationModel();
		location.setEventId(eventId);
		applyLocationRequest(location, request);
		return location;
	}

	private void applyLocationRequest(LocationModel location, EventRequest request) {
		location.setName(request.getLocation().getName());
		location.setStreet(request.getLocation().getStreet());
		location.setOtherStreet(request.getLocation().getOtherStreet());
		location.setCity(request.getLocation().getCity());
		location.setState(request.getLocation().getState());
		location.setCountry(request.getLocation().getCountry());
		location.setZipCode(request.getLocation().getZipCode());
	}
}
