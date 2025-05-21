package order_service.service;

import lombok.extern.slf4j.Slf4j;
import order_service.Event.OrderPlacedEvent;
import order_service.client.Feing_Inventory_client;
import order_service.dto.BaseResponse;
import order_service.dto.OrderItemsDto;
import order_service.dto.OrderRequest;
import order_service.model.Order;
import order_service.model.OrderItems;
import order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {
    @Autowired
    private  OrderRepository orderRepository;

    @Autowired
    private Feing_Inventory_client feing_inventory_client;

    @Autowired
    private KafkaTemplate <String, OrderPlacedEvent> kafkaTemplate;

    public Order placeOrder(OrderRequest orderRequest) {
        for (OrderItemsDto item : orderRequest.getOrderItemsList()) {
            String skuCode = item.getSkuCode();
            int quantity = item.getQuantity();

            try {
                ResponseEntity<BaseResponse> response = feing_inventory_client.getInventoryStatus(skuCode, quantity);

                if (response.getStatusCode().is2xxSuccessful()) {
                    BaseResponse baseResponse = response.getBody();

                    if (baseResponse != null && baseResponse.data instanceof Map) {
                        Map<?, ?> data = (Map<?, ?>) baseResponse.data;
                        Boolean isInStock = (Boolean) data.get("isInStock");

                        if (Boolean.FALSE.equals(isInStock)) {
                            log.warn("Item {} is out of stock", skuCode);
                            return null; // instead of throwing an exception
                        }
                    } else {
                        log.error("Invalid response from inventory for SKU: {}", skuCode);
                        return null;
                    }
                } else {
                    log.error("Inventory check failed for SKU: {} - {}", skuCode, response.getStatusCode());
                    return null;
                }
            } catch (Exception e) {
                log.error("Inventory service error for SKU: {}", skuCode, e);
                return null;
            }
        }

        // All items in stock, save order
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderTime(new Date());
        order.setEmail(orderRequest.getEmail());

        List<OrderItems> orderItems = orderRequest.getOrderItemsList().stream().map(dto -> {
            OrderItems item = new OrderItems();
            item.setSkuCode(dto.getSkuCode());
            item.setQuantity(dto.getQuantity());
            item.setPrice(dto.getPrice());
            return item;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);

        //send Message to Kafka topic

        OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
        orderPlacedEvent.setOrderStatus("ORDER_PLACED");
        orderPlacedEvent.setMessage("Order placed successfully");
        orderPlacedEvent.setOrderId(order.getOrderNumber());
        orderPlacedEvent.setEmail(order.getEmail());

        // Assuming you have a KafkaTemplate bean configured
log.info("Start - Sending OrderPlacedEvent to Kafka topic: {}", "order-placed");
        kafkaTemplate.send("order-placed", orderPlacedEvent);
        log.info("End - Sending OrderPlacedEvent to Kafka topic: {}", "order-placed");
        return orderRepository.save(order);
    }





    public Order getOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            log.info("Order retrieved successfully: {}", order);
        } else {
            log.warn("Order not found with id: {}", id);
        }
        return order;
    }
}
