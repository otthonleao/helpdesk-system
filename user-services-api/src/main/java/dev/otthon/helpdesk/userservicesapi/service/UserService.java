package dev.otthon.helpdesk.userservicesapi.service;

import dev.otthon.helpdesk.userservicesapi.mapper.UserMapper;
import dev.otthon.helpdesk.userservicesapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import model.exceptions.UserAlreadyExistsException;
import model.request.CreateUserRequest;
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

    private void isEmailAlreadyExists(final String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(
                    "DATA CONFLICT = User with email=[" + email + "] already exists."
            );
        }
    }

}
