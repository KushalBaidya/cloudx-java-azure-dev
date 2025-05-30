package io.swagger;

import com.chtrembl.petstore.order.model.ContainerEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;

@SpringBootApplication
@EnableCaching
@EnableSwagger2
@EnableCosmosRepositories(basePackages = "com.chtrembl.petstore.order.repository") // Enable Cosmos DB repositories
@EntityScan(basePackages = "com.chtrembl.petstore.order.model")
@ComponentScan(basePackages = { "io.swagger", "com.chtrembl.petstore.order.api", "io.swagger.configuration" ,"com.chtrembl.petstore.order.service","com.chtrembl.petstore.order.config" })
public class Swagger2SpringBoot implements CommandLineRunner {
    static final Logger log = LoggerFactory.getLogger(Swagger2SpringBoot.class);

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public ContainerEnvironment containerEnvvironment() {
        return new ContainerEnvironment();
    }

    @Override
    public void run(String... arg0) throws Exception {
        if (arg0.length > 0 && arg0[0].equals("exitcode")) {
            throw new ExitException();
        }
    }

    public static void main(String[] args) throws Exception {
        new SpringApplication(Swagger2SpringBoot.class).run(args);
    }

    class ExitException extends RuntimeException implements ExitCodeGenerator {
        private static final long serialVersionUID = 1L;

        @Override
        public int getExitCode() {
            return 10;
        }

    }
}
