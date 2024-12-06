package dev.otthon.helpdesk.userservicesapi.controller;

import dev.otthon.helpdesk.userservicesapi.entity.User;
import dev.otthon.helpdesk.userservicesapi.repository.UserRepository;
import model.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static dev.otthon.helpdesk.userservicesapi.creator.CreatorUtils.generateMock;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

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
}