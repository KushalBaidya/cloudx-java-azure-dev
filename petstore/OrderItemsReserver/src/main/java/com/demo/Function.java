package com.demo;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    private static final String BLOB_CONNECTION_STRING = System.getenv("BLOB_CONNECTION_STRING");
    private static final String CONTAINER_NAME = System.getenv("BLOB_CONTAINER_NAME");

    @FunctionName("reserveOrderItems")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<Map<String, Object>>> request,
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

        // Generate the order request JSON
        Map<String, Object> orderRequest = new HashMap<>();
        orderRequest.put("orderId", orderId);
        orderRequest.put("items", items);

        try {
            // Convert the order request to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String orderRequestJson = objectMapper.writeValueAsString(orderRequest);

            // Upload the JSON to Blob Storage
            uploadToBlobStorage(orderRequestJson, orderId, context);

            return request.createResponseBuilder(HttpStatus.OK)
                .body(String.format("Items for order ID %s have been reserved successfully.", orderId))
                .build();
        } catch (Exception e) {
            context.getLogger().severe("Error while processing order request: " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to process the order request.")
                .build();
        }
    }

    private void uploadToBlobStorage(String content, String sessionId, ExecutionContext context) {
        try {
            // Create a BlobServiceClient
            if (BLOB_CONNECTION_STRING == null || BLOB_CONNECTION_STRING.isEmpty()) {
                throw new RuntimeException("Environment variable 'BLOB_CONNECTION_STRING' is not set.");
            }
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(BLOB_CONNECTION_STRING)
                .buildClient();

            // Get the container client
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);

            // Create the container if it doesn't exist
            if (!containerClient.exists()) {
                containerClient.create();
                context.getLogger().info("Created Blob container: " + CONTAINER_NAME);
            }

            // Generate the blob name using the session ID
            String blobName = sessionId + ".json";

            // Get the blob client
            BlobClient blobClient = containerClient.getBlobClient(blobName);

            // Upload the content to the blob
            ByteArrayInputStream dataStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
            blobClient.upload(dataStream, content.length(), true);

            context.getLogger().info("Uploaded order request to Blob Storage: " + blobName);
        } catch (Exception e) {
            context.getLogger().severe("Error while uploading to Blob Storage: " + e.getMessage());
            throw new RuntimeException("Failed to upload to Blob Storage", e);
        }
    }
}
