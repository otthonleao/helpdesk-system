package dev.otthon.helpdesk.userservicesapi.controller;

import dev.otthon.helpdesk.userservicesapi.service.UserService;
import lombok.RequiredArgsConstructor;
import model.request.CreateUserRequest;
import model.response.UserResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody final CreateUserRequest createUserRequest) {
        UserResponse createdUser = userService.insert(createUserRequest);
        // Para gerar URI dinâmicas e mandar no header da resposta
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUser.id())
                .toUri();
        return ResponseEntity.created(uri).body(createdUser);
    }

}
