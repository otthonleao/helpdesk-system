package dev.otthon.helpdesk.orderservice.repository;

import dev.otthon.helpdesk.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
