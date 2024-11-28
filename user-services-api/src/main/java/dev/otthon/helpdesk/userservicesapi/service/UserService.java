package dev.otthon.helpdesk.userservicesapi.service;

import dev.otthon.helpdesk.userservicesapi.mapper.UserMapper;
import dev.otthon.helpdesk.userservicesapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import model.exceptions.UserAlreadyExistsException;
import model.request.CreateUserRequest;
import model.response.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse insert(final CreateUserRequest createUserRequest) {

        if (!userRepository.existsUserByEmail(createUserRequest.email())) {
            var toEntity = userMapper.fromRequest(createUserRequest);
            var savedDatabase = userRepository.save(toEntity);

            return userMapper.fromEntity(savedDatabase); // Mapeia o retorno para o DTO (UserResponse)
        }

        throw new UserAlreadyExistsException("User with email: " + createUserRequest.email()
                + " already exists. Type: " + CreateUserRequest.class.getName());
    }

}
