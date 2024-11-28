package dev.otthon.helpdesk.userservicesapi.mapper;

import dev.otthon.helpdesk.userservicesapi.entity.User;
import model.request.CreateUserRequest;
import model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = IGNORE, nullValueCheckStrategy = ALWAYS)
public interface UserMapper {

    @Mapping(target = "id", source = "id") // Mapeia o ID para o DTO de retorno
    UserResponse fromEntity(final User entity);

    @Mapping(target = "id", ignore = true)
    User fromRequest(final CreateUserRequest createUserRequest);

}
