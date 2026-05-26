package com.eoapi.app.Budget;

import com.eoapi.app.Budget.dto.BudgetItemRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1.0/budget-items")
public class BudgetItemController {

	private final BudgetItemService budgetItemService;

	public BudgetItemController(BudgetItemService budgetItemService) {
		this.budgetItemService = budgetItemService;
	}

	@GetMapping
	public ResponseEntity<List<BudgetItemModel>> getAll(@RequestParam Long eventId) {
		return ResponseEntity.ok(budgetItemService.findByEventId(eventId));
	}

	@GetMapping("/{id}")
	public ResponseEntity<BudgetItemModel> getById(@PathVariable Long id) {
		return ResponseEntity.ok(budgetItemService.findById(id));
	}

	@PostMapping
	public ResponseEntity<BudgetItemModel> create(@Valid @RequestBody BudgetItemRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(budgetItemService.create(request));
	}

	@PutMapping("/{id}")
	public ResponseEntity<BudgetItemModel> update(@PathVariable Long id,
			@Valid @RequestBody BudgetItemRequest request) {
		return ResponseEntity.ok(budgetItemService.update(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		budgetItemService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
