package com.eoapi.app.Users;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserModel, Long> {
	Optional<UserModel> findByEmail(String email);

	boolean existsByEmail(String email);
}
