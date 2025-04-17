package com.example.orderitemsreserver;

import com.microsoft.azure.functions.annotation.*;

import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import com.microsoft.azure.functions.*;

public class OrderItemsReserverFunction {
    @FunctionName("reserveOrderItems")
    public HttpResponseMessage run(
        @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<Map<String, Object>>> request,
        final ExecutionContext context) {

        context.getLogger().info("Processing order items reservation request...");

        // Parse the request body
        Map<String, Object> requestBody = request.getBody().orElse(new HashMap<>());
        String orderId = (String) requestBody.get("orderId");
        List<String> items = Optional.ofNullable(requestBody.get("items"))
            .filter(obj -> obj instanceof List<?>)
            .map(obj -> ((List<?>) obj).stream()
                .filter(item -> item instanceof String)
                .map(String.class::cast)
                .toList())
            .orElse(null);

        if (orderId == null || items == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                .body("Invalid request. Please provide 'orderId' and 'items'.")
                .build();
        }

        context.getLogger().info(String.format("Reserving items for order ID: %s", orderId));

        // Simulate reservation logic
        return request.createResponseBuilder(HttpStatus.OK)
            .body(String.format("Items for order ID %s have been reserved successfully.", orderId))
            .build();
    }
}