package com.eoapi.app.Security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class JwtConfig {

	@Bean
	public JwtDecoder jwtDecoder(JwtProperties jwtProperties) {
		SecretKey key = new SecretKeySpec(Base64.getUrlDecoder().decode(jwtProperties.getSecret()), "HmacSHA256");
		return NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
	}

	@Bean
	public JwtEncoder jwtEncoder(JwtProperties jwtProperties) {
		SecretKey key = new SecretKeySpec(Base64.getUrlDecoder().decode(jwtProperties.getSecret()), "HmacSHA256");
		OctetSequenceKey jwk = new OctetSequenceKey.Builder(key.getEncoded()).build();
		return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(jwk)));
	}
}
