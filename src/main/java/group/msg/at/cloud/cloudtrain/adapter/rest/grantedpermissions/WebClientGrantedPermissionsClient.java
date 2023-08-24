package group.msg.at.cloud.cloudtrain.adapter.rest.grantedpermissions;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

/**
 * Concrete {@code GrantedPermissionsClient} implementation based on {@code WebClient}.
 */
// @Component
public class WebClientGrantedPermissionsClient implements GrantedPermissionsClient {

    /**
     * Inject a {@code WebClient.Builder} instead of a {@code WebClient}
     * to be able to create a WebClient with some common configuration and
     * some endpoint specific configuration.
     */
    @Autowired
    WebClient.Builder webClientBuilder;

    /**
     * Inject configuration property with downstream service base URL.
     */
    @Value("${cloudtrain.services.downstream.url}")
    String downstreamServiceUrl;

    /**
     * WebClient to be used to call downstream service.
     */
    WebClient webClient;

    private static final ParameterizedTypeReference<List<GrantedPermission>> typeRef = new ParameterizedTypeReference<List<GrantedPermission>>() {};

    /**
     * Special constructor accepting all dependencies.
     * @param webClientBuilder
     * @param downstreamServiceUrl
     */
    public WebClientGrantedPermissionsClient(WebClient.Builder webClientBuilder, String downstreamServiceUrl) {
        this.webClientBuilder = webClientBuilder;
        this.downstreamServiceUrl = downstreamServiceUrl;
    }

    /**
     * Build a {@code WebClient} using the provided {@code WebClient.Builder}.
     */
    @PostConstruct
    public void onPostConstruct() {
        webClient = webClientBuilder.baseUrl(downstreamServiceUrl).build();
    }

    @Override
    public List<GrantedPermission> getGrantedPermissionsByCurrentUser() {
        return webClient
                .get()
                .uri("/api/v1/grantedPermissions")
                .retrieve()
                .toEntity(typeRef)
                .block()
                .getBody();
    }
}
