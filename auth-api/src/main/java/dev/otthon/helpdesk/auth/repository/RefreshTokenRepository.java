package dev.otthon.helpdesk.auth.repository;

import dev.otthon.helpdesk.auth.model.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
}
