package dev.otthon.helpdesk.orderservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.OrderStatusEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "TB_ORDER")
public class Order implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 45)
    private String requesterId;

    @Column(nullable = false, length = 45)
    private String customerId;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 3000)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status = OrderStatusEnum.OPEN;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime closedAt;


}
