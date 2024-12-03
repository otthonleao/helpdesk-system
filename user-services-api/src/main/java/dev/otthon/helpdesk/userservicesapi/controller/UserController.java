package dev.otthon.helpdesk.userservicesapi.controller;

import dev.otthon.helpdesk.userservicesapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import model.exceptions.ResourceNotFoundException;
import model.exceptions.UserAlreadyExistsException;
import model.request.CreateUserRequest;
import model.request.UpdateUserRequest;
import model.response.UserResponse;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@Tag(name = "UserController", description = "Controller for user operations")
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "User already exists", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserAlreadyExistsException.class)))
    })
    @PostMapping
    public ResponseEntity<UserResponse> create(
            @Valid
            @Parameter(description = "User id", required = true, example = "674808b02ff2181e545a8778")
            @RequestBody final CreateUserRequest createUserRequest) {

        UserResponse createdUser = userService.insert(createUserRequest);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUser.id())
                .toUri();
        return ResponseEntity.created(uri).body(createdUser);
    }

    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResourceNotFoundException.class))),
            @ApiResponse(responseCode = "409", description = "User already exists", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserAlreadyExistsException.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable entity", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @Valid
            @PathVariable(name = "id") final String id,
            @RequestBody final UpdateUserRequest updateUserRequest) {

        UserResponse updatedUser = userService.update(id, updateUserRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Find user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResourceNotFoundException.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{id}")
    ResponseEntity<UserResponse> findById(
            @Parameter(description = "User id", required = true, example = "674808b02ff2181e545a8778")
            @PathVariable(name = "id") final String id) {
        return ResponseEntity.ok(userService.findById(id));
    }

}
