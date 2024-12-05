package dev.otthon.helpdesk.userservicesapi.service;

import dev.otthon.helpdesk.userservicesapi.entity.User;
import dev.otthon.helpdesk.userservicesapi.mapper.UserMapper;
import dev.otthon.helpdesk.userservicesapi.repository.UserRepository;
import model.exceptions.ResourceNotFoundException;
import model.exceptions.UserAlreadyExistsException;
import model.request.CreateUserRequest;
import model.request.UpdateUserRequest;
import model.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static dev.otthon.helpdesk.userservicesapi.creator.CreatorUtils.generateMock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        when(userRepository.findById(anyString())).thenReturn(Optional.of(new User()));
        when(userMapper.fromEntity(any(User.class))).thenReturn(generateMock(UserResponse.class));

        final var response = userService.findById("1");

        assertNotNull(response);
        assertEquals(UserResponse.class, response.getClass());

        verify(userRepository, times(1)).findById(anyString());
        verify(userMapper, times(1)).fromEntity(any(User.class));
    }

    @Test
    void whenCallFindByIdWithInvalidIdThenThrowResourceNotFoundException() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());
        try {
            userService.findById("1");
            fail("ResourceNotFoundException not thrown");
        } catch (Exception e) {
            assertEquals(ResourceNotFoundException.class, e.getClass());
            assertEquals("Object with ID: 1 not found , Type: " + UserResponse.class.getSimpleName(), e.getMessage());
        }

    }

    @Test
    void whenCallFindAllThenReturnListOfUserResponse() {
        when(userRepository.findAll()).thenReturn(java.util.List.of(new User(), new User()));
        when(userMapper.fromEntity(any(User.class))).thenReturn(mock(UserResponse.class));

        final var response = userService.findAll();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(UserResponse.class, response.get(0).getClass());

        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(2)).fromEntity(any(User.class));
    }

    @Test
    void testInsertShouldReturnUserResponseOnSuccess() {
        final var request = generateMock(CreateUserRequest.class);

        when(userMapper.fromRequest(any())).thenReturn(new User());
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("password");
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(userMapper.fromEntity(any(User.class))).thenReturn(generateMock(UserResponse.class));

        final var response = userService.insert(request);

        assertNotNull(response);
        assertEquals(UserResponse.class, response.getClass());

        verify(userMapper).fromRequest(request);
        verify(userRepository).existsByEmail(request.email());
        verify(passwordEncoder).encode(request.password());
        verify(userRepository).save(any(User.class));

    }

    @Test
    void testInsertShouldThrowExceptionWhenUserAlreadyExists() {
        final var request = generateMock(CreateUserRequest.class);

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        try {
            userService.insert(request);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertEquals(UserAlreadyExistsException.class, e.getClass());
            assertEquals("DATA CONFLICT = User with email=[" + request.email() + "] already exists.", e.getMessage());
        }

        verify(userRepository).existsByEmail(request.email());
        verify(userMapper, times(0)).fromRequest(request);
        verify(passwordEncoder, times(0)).encode(request.password());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void testUpdateWithInvalidIdShouldThrowResourceNotFoundException() {
        final var request = generateMock(UpdateUserRequest.class);

        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        try {
            userService.update("1", request);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertEquals(ResourceNotFoundException.class, e.getClass());
            assertEquals("Object with ID: 1 not found , Type: " + UserResponse.class.getSimpleName(), e.getMessage());
        }

        verify(userRepository).findById("1");
        verify(userMapper, times(0)).update(any(), any());
        verify(passwordEncoder, times(0)).encode(request.password());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void testUpdateWithEmailBelongAnotherUserShouldThrowUserAlreadyExistsException() {
        final var request = generateMock(UpdateUserRequest.class);
        final var entity = generateMock(User.class);

        when(userRepository.findById(anyString())).thenReturn(Optional.of(entity));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(entity));

        try {
            userService.update("1", request);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertEquals(UserAlreadyExistsException.class, e.getClass());
            assertEquals("DATA VIOLATION = Email [ " + request.email() + " ] already exists for another user.", e.getMessage());
        }

        verify(userRepository).findById(anyString());
        verify(userRepository).findByEmail(request.email());
        verify(userMapper, times(0)).update(any(), any());
        verify(passwordEncoder, times(0)).encode(request.password());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void testUpdateShouldReturnUserResponseOnSuccess() {
        final var id = "1";
        final var request = generateMock(UpdateUserRequest.class);
        final var entity = generateMock(User.class).withId(id);

        when(userRepository.findById(anyString())).thenReturn(Optional.of(entity));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(entity));
        when(userMapper.update(any(), any())).thenReturn(entity);
        when(passwordEncoder.encode(anyString())).thenReturn("password");
        when(userRepository.save(any(User.class))).thenReturn(entity);
        when(userMapper.fromEntity(any(User.class))).thenReturn(generateMock(UserResponse.class));

        final var response = userService.update(id, request);

        assertNotNull(response);
        assertEquals(UserResponse.class, response.getClass());

        verify(userRepository).findById(anyString());
        verify(userRepository).findByEmail(request.email());
        verify(userMapper).update(request, entity);
        verify(passwordEncoder).encode(request.password());
        verify(userMapper).fromEntity(any(User.class));
        verify(userRepository).save(any(User.class));
    }

}