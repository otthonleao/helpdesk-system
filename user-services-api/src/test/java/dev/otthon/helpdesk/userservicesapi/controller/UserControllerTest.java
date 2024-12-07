package dev.otthon.helpdesk.userservicesapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.otthon.helpdesk.userservicesapi.entity.User;
import dev.otthon.helpdesk.userservicesapi.repository.UserRepository;
import model.request.CreateUserRequest;
import model.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static dev.otthon.helpdesk.userservicesapi.creator.CreatorUtils.generateMock;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    public static final String BASE_URI = "/api/user";
    public static final String VALID_EMAIL = "already_exists@email.com";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByIdWithSucess() throws Exception {

        final var entity = generateMock(User.class);
        final var userId = userRepository.save(entity).getId();

        mockMvc.perform(get("/api/user/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value(entity.getName()))
                .andExpect(jsonPath("$.email").value(entity.getEmail()))
                .andExpect(jsonPath("$.password").value(entity.getPassword()))
                .andExpect(jsonPath("$.profiles").isArray());
        userRepository.deleteById(userId);
    }

    @Test
    void testFindByIdWithNotFound() throws Exception {
        mockMvc.perform(get("/api/user/{id}", "123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(NOT_FOUND.value()))
                .andExpect(jsonPath("$.title").value("Entity not found in database"))
                .andExpect(jsonPath("$.detail").value("Object with ID: 123 not found , Type: " + UserResponse.class.getSimpleName()))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.errorSourceClass").exists())
                .andExpect(jsonPath("$.errorSourceMethod").exists());
    }

    @Test
    void testInsertWithSuccess() throws Exception {
        final var validEmail = VALID_EMAIL;
        final var request = generateMock(CreateUserRequest.class)
                .withEmail(validEmail);

        mockMvc.perform(post(BASE_URI)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(request))).andExpect(status().isCreated());
        userRepository.deleteByEmail(validEmail);
    }

    private String toJson(final Object object) throws Exception {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new Exception("Error on convert object to json", e);
        }
    }

    @Test
    void testInsertWithConflict() throws Exception {
        final var request = generateMock(CreateUserRequest.class)
                .withEmail(VALID_EMAIL);

        mockMvc.perform(post(BASE_URI)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(request))).andExpect(status().isConflict());
        userRepository.deleteByEmail(VALID_EMAIL);
    }

    @Test
    void testInsertUserWithNameEmptyThenThrowUnprocessableEntity() throws Exception{
        final var request = generateMock(CreateUserRequest.class).withName("").withEmail(VALID_EMAIL);

        mockMvc.perform(post(BASE_URI)
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
                ).andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(UNPROCESSABLE_ENTITY.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
//                .andExpect(jsonPath("$.errors[?(@.fieldName=='name' && @.message=='Name must contain between 3 and 50 characters')]").exists());
    }
}