package com.GujjuSajang.order.dto;

import com.GujjuSajang.order.entity.Orders;
import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrdersDto {

    private Long id;
    private int totalPrice;
    private Timestamp createdAt;

    public static OrdersDto from(Orders orders) {
        return OrdersDto.builder()
                .id(orders.getId())
                .totalPrice(orders.getTotalPrice())
                .createdAt(Timestamp.valueOf(orders.getCreateAt()))
                .build();
    }

}
