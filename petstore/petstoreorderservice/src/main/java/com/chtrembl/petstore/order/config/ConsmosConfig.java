package com.chtrembl.petstore.order.config;

import com.azure.cosmos.CosmosClientBuilder;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.config.CosmosConfig;
import com.azure.spring.data.cosmos.core.ResponseDiagnostics;
import com.azure.spring.data.cosmos.core.ResponseDiagnosticsProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

@Configuration
public class ConsmosConfig extends AbstractCosmosConfiguration {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ConsmosConfig.class);

    @Value("${spring.cloud.azure.cosmos.uri}")
    private String cosmosDbUri;

    @Value("${spring.cloud.azure.cosmos.key}")
    private String cosmosDbKey;

    @Value("${spring.cloud.azure.cosmos.database}")
    private String cosmosDbName;

    @Override
    protected String getDatabaseName() {
        return cosmosDbName; // Use the database name from application.yml
    }

    @Bean
    public CosmosClientBuilder cosmosClientBuilder() {
        return new CosmosClientBuilder()
                .endpoint(cosmosDbUri) // Use the URI from application.yml
                .key(cosmosDbKey) // Use the key from application.yml
                .directMode(); // Use direct mode for better performance
    }

    @Bean
    public CosmosConfig cosmosConfig() {
        return CosmosConfig.builder()
                .responseDiagnosticsProcessor(responseDiagnosticsProcessor())
                .enableQueryMetrics(true)
                .build();
    }

    @Bean
    public ResponseDiagnosticsProcessor responseDiagnosticsProcessor() {
        return new ResponseDiagnosticsProcessorImplementation();
    }

    private static class ResponseDiagnosticsProcessorImplementation implements ResponseDiagnosticsProcessor {

        @Override
        public void processResponseDiagnostics(@Nullable ResponseDiagnostics responseDiagnostics) {
            logger.info("Response Diagnostics {}", responseDiagnostics);
        }
    }
}