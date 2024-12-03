package dev.otthon.helpdesk.userservicesapi.service;

import dev.otthon.helpdesk.userservicesapi.entity.User;
import dev.otthon.helpdesk.userservicesapi.mapper.UserMapper;
import dev.otthon.helpdesk.userservicesapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import model.exceptions.ResourceNotFoundException;
import model.exceptions.UserAlreadyExistsException;
import model.request.CreateUserRequest;
import model.request.UpdateUserRequest;
import model.response.UserResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse insert(final CreateUserRequest createUserRequest) {
        isEmailAlreadyExists(createUserRequest.email());
        var toEntity = userMapper.fromRequest(createUserRequest)
                .withPassword(passwordEncoder.encode(createUserRequest.password()));
        var savedDatabase = userRepository.save(toEntity);
        return userMapper.fromEntity(savedDatabase); // Mapeia o retorno para o DTO (UserResponse)
    }

    @Transactional
    public UserResponse update(final String id, final UpdateUserRequest request) {
        User entity = find(id);
        checkIfEmailBelongsToAnotherUser(request.email(), id);
        return userMapper.fromEntity(
                userRepository.save(
                        userMapper.update(request, entity)
                                .withPassword(request.password() != null ? passwordEncoder.encode(request.password()) : entity.getPassword())
                )
        );
    }

    private User find(final String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Object not found. Id: " + id + ", Type: " + UserResponse.class.getSimpleName()
                ));
    }

    private void checkIfEmailBelongsToAnotherUser(final String email, final String id) {
        userRepository.findByEmail(email)
                .filter(user -> !user.getId().equals(id))
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException("DATA VIOLATION = Email [ " + email + " ] already exists for another user.");
                });
    }

    private void isEmailAlreadyExists(final String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(
                    "DATA CONFLICT = User with email=[" + email + "] already exists."
            );
        }
    }
}
