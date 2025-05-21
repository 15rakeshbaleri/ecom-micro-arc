package order_service.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemsDto {
    private String skuCode;
    private Integer quantity;
    private BigDecimal price;
}
