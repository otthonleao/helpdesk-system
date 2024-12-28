package dev.otthon.helpdesk.orderservice.service;

import dev.otthon.helpdesk.orderservice.entity.Order;
import dev.otthon.helpdesk.orderservice.mapper.OrderMapper;
import dev.otthon.helpdesk.orderservice.repository.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import model.enums.OrderStatusEnum;
import model.exceptions.ResourceNotFoundException;
import model.request.CreateOrderRequest;
import model.request.UpdateOrderRequest;
import model.response.OrderResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Log4j2
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;

    public void deleteById(final Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Object not found. Id: " + id + ", Type: " + Order.class.getSimpleName());
        }
        repository.deleteById(id);
    }

    public OrderResponse update(final Long id, UpdateOrderRequest request) {
        Order entity = findById(id);
        entity = mapper.fromRequest(entity, request);

        if (entity.getStatus().equals(OrderStatusEnum.FINISHED.getDescription()) && entity.getClosedAt() == null) {
            entity.setClosedAt(LocalDateTime.now());
        }

        return mapper.fromEntity(repository.save(entity));
    }

    public Order findById(final Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Object not found. Id: " + id + ", Type: " + Order.class.getSimpleName()
                ));
    }

    public void save(CreateOrderRequest request) {
        final var entity = repository.save(mapper.fromRequest(request));
        log.info("Order created: {} successfully", entity);
    }
}
