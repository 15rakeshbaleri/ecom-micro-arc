package order_service.dto;

import lombok.Data;
import order_service.model.OrderItems;

import java.util.List;
@Data
public class OrderRequest {
    private String Email;
    private List<OrderItemsDto> orderItemsList;


}
