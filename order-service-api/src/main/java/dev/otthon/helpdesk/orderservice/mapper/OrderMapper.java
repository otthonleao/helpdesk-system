package dev.otthon.helpdesk.orderservice.mapper;

import dev.otthon.helpdesk.orderservice.entity.Order;
import model.enums.OrderStatusEnum;
import model.request.CreateOrderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = IGNORE,
        nullValueCheckStrategy = ALWAYS
)
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStatus")
    @Mapping(target = "createdAt", expression = "java(mapCreatedAt())")
    Order fromRequest(CreateOrderRequest request);

    @Named("mapStatus")
    default OrderStatusEnum mapStatus(final String status) {
        return OrderStatusEnum.toEnum(status);
    }

    default LocalDateTime mapCreatedAt() {
        return LocalDateTime.now();
    }



}
