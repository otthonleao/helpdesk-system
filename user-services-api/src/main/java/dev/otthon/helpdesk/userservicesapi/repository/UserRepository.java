package dev.otthon.helpdesk.userservicesapi.repository;

import dev.otthon.helpdesk.userservicesapi.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(final String email);


}
