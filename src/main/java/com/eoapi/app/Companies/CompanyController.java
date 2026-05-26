package com.eoapi.app.Companies;

import com.eoapi.app.Companies.dto.CompanyRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1.0/companies")
public class CompanyController {

	private final CompanyService companyService;

	public CompanyController(CompanyService companyService) {
		this.companyService = companyService;
	}

	@GetMapping
	public ResponseEntity<List<CompanyModel>> getAll() {
		return ResponseEntity.ok(companyService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<CompanyModel> getById(@PathVariable Long id) {
		return ResponseEntity.ok(companyService.findById(id));
	}

	@PostMapping
	public ResponseEntity<CompanyModel> create(@Valid @RequestBody CompanyRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(companyService.create(request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		companyService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
