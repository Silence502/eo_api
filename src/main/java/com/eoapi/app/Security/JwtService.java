package com.eoapi.app.Security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {

	private final JwtEncoder jwtEncoder;
	private final JwtDecoder jwtDecoder;
	private final JwtProperties jwtProperties;

	public JwtService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, JwtProperties jwtProperties) {
		this.jwtEncoder = jwtEncoder;
		this.jwtDecoder = jwtDecoder;
		this.jwtProperties = jwtProperties;
	}

	public String generateAccessToken(UserDetails userDetails) {
		return generate(userDetails, "access", jwtProperties.getAccessTokenExpiration());
	}

	public String generateRefreshToken(UserDetails userDetails) {
		return generate(userDetails, "refresh", jwtProperties.getRefreshTokenExpiration());
	}

	private String generate(UserDetails userDetails, String type, long expirationMs) {
		Instant now = Instant.now();
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.subject(userDetails.getUsername())
				.issuedAt(now)
				.expiresAt(now.plusMillis(expirationMs))
				.claim("type", type)
				.build();
		JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
		return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
	}

	public String extractUsername(String token) {
		return jwtDecoder.decode(token).getSubject();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		try {
			Jwt jwt = jwtDecoder.decode(token);
			return userDetails.getUsername().equals(jwt.getSubject());
		} catch (JwtException e) {
			return false;
		}
	}

	public boolean isAccessToken(String token) {
		try {
			Jwt jwt = jwtDecoder.decode(token);
			return "access".equals(jwt.getClaim("type"));
		} catch (JwtException e) {
			return false;
		}
	}
}
