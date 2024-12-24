package dev.otthon.helpdesk.orderservice.service;

import dev.otthon.helpdesk.orderservice.mapper.OrderMapper;
import dev.otthon.helpdesk.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import model.request.CreateOrderRequest;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;

    public void save(CreateOrderRequest request) {
        final var entity = repository.save(mapper.fromRequest(request));
        log.info("Order created: {} successfully", entity);
    }

}
