package group.msg.at.cloud.cloudtrain.adapter.rest.grantedpermissions;

import org.springframework.http.MediaType;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

/**
 * Common interface of a REST client for the downstream granted permissions service.
 * <p>
 * Concrete instances of this interface are usually created using a {@code @Bean} method in a Spring Boot
 * {@code @Configuration} bean.
 * </p>
 * <p>
 * Please note that the Http Interface annotations are only active when a proxy has been created fot this interface
 * using a {@code HttpServiceProxyFactory}.
 * </p>
 *
 * @see GrantedPermissionsClientConfiguration
 */
@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface GrantedPermissionsClient {

    @GetExchange("/api/v1/grantedPermissions")
    List<GrantedPermission> getGrantedPermissionsByCurrentUser();
}
