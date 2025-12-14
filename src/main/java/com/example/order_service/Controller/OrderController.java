package com.example.order_service.Controller;

import com.example.order_service.dto.ProductResponse;
import com.example.order_service.model.Order;
import com.example.order_service.repository.OrderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderRepository orderRepository;
    private final org.springframework.web.reactive.function.client.WebClient.Builder webClientBuilder;

    public OrderController(OrderRepository orderRepository, org.springframework.web.reactive.function.client.WebClient.Builder webClientBuilder) {
        this.orderRepository = orderRepository;
        this.webClientBuilder = webClientBuilder;
    }

    @PostMapping
    public String placeOrder(@RequestBody Order order) {
        
        // 1. Get the SKU Code from the user's order request
        String skuCode = order.getSkuCode(); 

        // 2. Call Product Service to search for this specific product
        // We assume Product Service has an endpoint like /api/product?name=skuCode
        // For now, let's keep it simple and just fetch ALL products and filter (Not efficient, but easy for learning)
        
        Boolean productExists = webClientBuilder.build().get()
                        .uri("http://product-service/api/product") // Calls the neighbor
                        .retrieve()
                        .bodyToFlux(ProductResponse.class)
                        .collectList()
                        .block()
                        .stream()
                        .anyMatch(p -> p.getName().equals(skuCode)); // Does the name match?
        if (productExists) {
            order.setOrderNumber(UUID.randomUUID().toString());
            orderRepository.save(order);
            return "Order Placed Successfully";
        } else {
            throw new IllegalArgumentException("Product not found in catalog");
        }
    }
}
