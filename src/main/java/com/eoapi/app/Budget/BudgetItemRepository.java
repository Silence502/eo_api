package com.eoapi.app.Budget;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BudgetItemRepository extends CrudRepository<BudgetItemModel, Long> {

	List<BudgetItemModel> findByEventId(Long eventId);
}
