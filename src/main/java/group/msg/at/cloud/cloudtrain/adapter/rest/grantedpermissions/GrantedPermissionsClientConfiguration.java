package group.msg.at.cloud.cloudtrain.adapter.rest.grantedpermissions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * Configuration bean which decides what concrete implementation of {@code GrantedPermissionsClient} to use.
 * <p>
 * Note: This configuration bean has only be added to demonstrate the REST client options. In a real world
 * application you always would agree on one option and stick to it.
 * </p>
 */
@Configuration
public class GrantedPermissionsClientConfiguration {

    @Value("${cloudtrain.rest.client.strategy:HTTP_INTERFACE}")
    String restClientStrategy;

    @Bean
    GrantedPermissionsClient grantedPermissionsClient(
            RestTemplateBuilder restTemplateBuilder,
            WebClient.Builder webClientBuilder,
            @Value("${cloudtrain.services.downstream.url}") String downstreamUrl
    ) {
        GrantedPermissionsClient result = null;
        switch(restClientStrategy) {
            case "WEB_CLIENT":
                result = new WebClientGrantedPermissionsClient(webClientBuilder, downstreamUrl);
                break;
            case "REST_TEMPLATE":
                result = new RestTemplateGrantedPermissionsClient(restTemplateBuilder, downstreamUrl);
                break;
            case "HTTP_INTERFACE":
                WebClient webClient = webClientBuilder.baseUrl(downstreamUrl).build();
                HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient)).build();
                result = proxyFactory.createClient(GrantedPermissionsClient.class);
                break;
            default:
                throw new IllegalStateException(String.format("Invalid rest client strategy [%s]", restClientStrategy));
        }
        return result;
    }

}
