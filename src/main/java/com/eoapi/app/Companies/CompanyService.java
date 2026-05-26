package com.eoapi.app.Companies;

import com.eoapi.app.Companies.dto.CompanyRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyService {

	private final CompanyRepository companyRepository;

	public CompanyService(CompanyRepository companyRepository) {
		this.companyRepository = companyRepository;
	}

	public List<CompanyModel> findAll() {
		List<CompanyModel> companies = new ArrayList<>();
		companyRepository.findAll().forEach(companies::add);
		return companies;
	}

	public CompanyModel findById(Long id) {
		return companyRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
	}

	public CompanyModel create(CompanyRequest request) {
		CompanyModel company = new CompanyModel();
		mapToModel(company, request);
		return companyRepository.save(company);
	}

	public void delete(Long id) {
		if (!companyRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
		}
		companyRepository.deleteById(id);
	}

	private void mapToModel(CompanyModel company, CompanyRequest request) {
		company.setName(request.getName());
		company.setVatNumber(request.getVatNumber());
		if (request.getCurrencyCode() != null)
			company.setCurrencyCode(request.getCurrencyCode());
		company.setStreet(request.getStreet());
		company.setOtherStreet(request.getOtherStreet());
		company.setCity(request.getCity());
		company.setCountry(request.getCountry());
		company.setState(request.getState());
		company.setZipCode(request.getZipCode());
		company.setPhone(request.getPhone());
		company.setEmail(request.getEmail());
		company.setWebsite(request.getWebsite());
		company.setNotes(request.getNote());
		company.setLogo(request.getLogo());
	}
}
