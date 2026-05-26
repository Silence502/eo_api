package com.eoapi.app.Budget;

import com.eoapi.app.Budget.dto.BudgetItemRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BudgetItemService {

	private final BudgetItemRepository budgetItemRepository;

	public BudgetItemService(BudgetItemRepository budgetItemRepository) {
		this.budgetItemRepository = budgetItemRepository;
	}

	public List<BudgetItemModel> findByEventId(Long eventId) {
		return budgetItemRepository.findByEventId(eventId);
	}

	public BudgetItemModel findById(Long id) {
		return budgetItemRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Budget item not found"));
	}

	public BudgetItemModel create(BudgetItemRequest request) {
		BudgetItemModel item = new BudgetItemModel();
		item.setEventId(request.getEventId());
		return mapAndSave(item, request);
	}

	public BudgetItemModel update(Long id, BudgetItemRequest request) {
		BudgetItemModel item = findById(id);
		return mapAndSave(item, request);
	}

	public void delete(Long id) {
		if (!budgetItemRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Budget item not found");
		}
		budgetItemRepository.deleteById(id);
	}

	private BudgetItemModel mapAndSave(BudgetItemModel item, BudgetItemRequest request) {
		if (request.getCurrencyCode() != null)
			item.setCurrencyCode(request.getCurrencyCode());
		item.setCategory(request.getCategory());
		item.setLabel(request.getLabel());
		if (request.getEstimatedAmount() != null)
			item.setEstimatedAmount(request.getEstimatedAmount());
		if (request.getActualAmount() != null)
			item.setActualAmount(request.getActualAmount());
		if (request.getPaidAmount() != null)
			item.setPaidAmount(request.getPaidAmount());
		item.setDueDate(request.getDueDate());
		if (request.getStatus() != null)
			item.setStatus(request.getStatus());
		item.setNotes(request.getNotes());
		return budgetItemRepository.save(item);
	}
}
