package com.boilerplate.auth.repository;


import com.boilerplate.auth.model.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	Optional<RefreshToken> findByPayload(String payload);

	void deleteByPayload(String payload);
}
