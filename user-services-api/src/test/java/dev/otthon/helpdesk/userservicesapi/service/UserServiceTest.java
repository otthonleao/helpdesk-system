package dev.otthon.helpdesk.userservicesapi.service;

import dev.otthon.helpdesk.userservicesapi.entity.User;
import dev.otthon.helpdesk.userservicesapi.mapper.UserMapper;
import dev.otthon.helpdesk.userservicesapi.repository.UserRepository;
import model.response.UserResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void whenCallFindByIdWithValidIdThenReturnUser() {
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(new User()));
        Mockito.when(userMapper.fromEntity(Mockito.any(User.class))).thenReturn(Mockito.mock(UserResponse.class));

        final var response = userService.findById("1");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(UserResponse.class, response.getClass());

        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyString());
        Mockito.verify(userMapper, Mockito.times(1)).fromEntity(Mockito.any(User.class));
    }

}