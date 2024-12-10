package dev.otthon.helpdesk.auth.service;

import dev.otthon.helpdesk.auth.model.RefreshToken;
import dev.otthon.helpdesk.auth.repository.RefreshTokenRepository;
import dev.otthon.helpdesk.auth.security.dto.UserDetailsDTO;
import dev.otthon.helpdesk.auth.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import model.exceptions.RefreshTokenExpired;
import model.exceptions.ResourceNotFoundException;
import model.response.RefreshTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${jwt.expiration.refresh-token.seconds}")
    private Long refreshTokenExpirationSeconds;

    private final RefreshTokenRepository repository;
    private final UserDetailsService userDetailsService;
    private final JWTUtils jwtUtils;

    public RefreshToken save(final String username) {
        return repository.save(
                RefreshToken.builder()
                        .id(UUID.randomUUID().toString())
                        .createdAt(LocalDateTime.now())
                        .expiresAt(LocalDateTime.now().plusSeconds(refreshTokenExpirationSeconds))
                        .username(username)
                        .build()
        );
    }

    public RefreshTokenResponse refreshToken(final String refreshTokenId) {
        final var refreshToken = repository.findById(refreshTokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found. Id: " + refreshTokenId));
        if(refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RefreshTokenExpired("Refresh token expired. Id: " + refreshTokenId);
        }

        return new RefreshTokenResponse(
                jwtUtils.generateToken((UserDetailsDTO) userDetailsService.loadUserByUsername(refreshToken.getUsername()))
        );
    }

}
