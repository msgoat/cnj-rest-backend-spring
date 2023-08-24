# cnj-rest-backend-spring

Cloud native Java backend based on Spring Boot using Spring WEB MVC to expose and consume REST endpoints.

## Status

![Build status](https://codebuild.eu-west-1.amazonaws.com/badges?uuid=eyJlbmNyeXB0ZWREYXRhIjoid2QwdUFvcnlReWl1UmtGbHJFdEl3S1hnZFFMa3VDRHFKa3FnUHlUbkxZSERSbUFsMDFtY05XbFJLTmpRb0JXdEwrMnp2NUxEUy9ZNGlDWlRkcGNtdXU0PSIsIml2UGFyYW1ldGVyU3BlYyI6Im1KUXhwUUZLVnYwWG1HM2EiLCJtYXRlcmlhbFNldFNlcmlhbCI6MX0%3D&branch=main)

## Release Information

A changelog can be found in [changelog.md](changelog.md).

## Docker Pull Command

`docker pull docker.cloudtrain.aws.msgoat.eu/cloudtrain/cnj-rest-backend-spring`

## Synopsis

### REST clients

Since version 3, Spring Boot offers three ways to consume REST endpoints:

* `RestTemplate`: you provide a REST client bean which maps Java method invocations to RestTemplate calls to send HTTP
  requests and to receive HTTP responses synchronously. Concrete RestTemplate instances are always build from an injected
  RestTemplateBuilder to allow application wide customizations with interceptors and customizers.
* `WebClient`: you provide a REST client bean which maps Java method invocations to WebClient calls to send HTTP
  requests and to receive HTTP responses either asynchronously or synchronously. Concrete WebClient instances are always build from an injected WebClientBuilder
  to allow application wide customizations with interceptors and customizers. In contrast to RestTemplate, WebClient exposes a fluent API and allows you
  to build reactive REST clients.
* `HTTP interfaces`: you provide an annotated Java interface in combination with a WebClient instance to send HTTP
  requests and to receive HTTP responses either asynchronously or synchronously. Concrete WebClient instances are always build from an injected WebClientBuilder
  to allow application wide customizations with interceptors and customizers.

> __Attention__: `RestTemplate` is in maintenance mode since Spring version 5 and can be considered as deprecated!

The usage of each REST client strategy is demonstrated by this showcase. The actual REST client strategy used
can be controlled by setting configuration property `cloudtrain.rest.client.strategy` to one of the following values:
* `HTTP_INTERFACE` (default): will use HTTP interfaces, WebClient and HTTP service proxies as REST clients
  (see [GrantedPermissionsClient](src/main/java/group/msg/at/cloud/cloudtrain/adapter/rest/grantedpermissions/GrantedPermissionsClient.java))
* `WEB_CLIENT`: will use WebClient as REST clients
  (see [WebClientGrantedPermissionsClient](src/main/java/group/msg/at/cloud/cloudtrain/adapter/rest/grantedpermissions/WebClientGrantedPermissionsClient.java))
* `REST_TEMPLATE`: will use RestTemplate as REST clients
  (see [RestTemplateGrantedPermissionsClient](src/main/java/group/msg/at/cloud/cloudtrain/adapter/rest/grantedpermissions/RestTemplateGrantedPermissionsClient.java))

> __Advice__: Always stick to one strategy for REST clients in your Spring Boot applications.
> If you are on Spring Boot 3+, consider using HTTP interfaces.
> If you are on Spring Boot 2, you definitely should use WebClients.
> If you are still using RestTemplate, strongly consider to replace them with WebClients or HTTP interfaces.

### Forwarding JWT tokens to downstream services

Unfortunately, Spring Security does not provide automatic propagation of JWT tokens to downstream services.
Thus, you will have to come up with some custom interceptors or filters which add JWT tokens as headers to outbound
REST requests.

In this demo, propagation of JWT tokens is automatically handled by library `cnj-common-security-oidc-spring`.
See repo [cnj-common-security-oidc-spring](https://github.com/msgoat/cnj-common-security-oidc-spring) for more details.

### Observing inbound and outbound REST messages

In a common microservice scenario with lots of microservices it's easy to lose track of REST messages exchanged between
these microservices. To improve the observability of Spring Boot microservices - especially in the development and 
testing phase - library `cnj-common-observability-spring` automatically adds some useful features like REST message
tracing to your Spring Boot application. 
See repo [cnj-common-observability-spring](https://github.com/msgoat/cnj-common-observability-spring) for more details.

## HOW-TO build this application locally

If all prerequisites are met, just run the following Maven command in the project folder:

```shell 
mvn clean verify -P pre-commit-stage
```

Build results: a Docker image containing the showcase application.

## HOW-TO start and stop this showcase locally

In order to run the whole showcase locally, just run the following docker commands in the project folder:

```shell 
docker compose up -d
docker compose logs -f 
```

Press `Ctlr+c` to stop tailing the container logs and run the following docker command to stop the showcase:

```shell 
docker compose down
```

## HOW-TO demo this showcase

The showcase application will be accessible:
* locally via `http://localhost:38080`
* remotely via `https://train2023-dev.k8s.cloudtrain.aws.msgoat.eu/cloudtrain/cnj-rest-backend-spring` (if the training cluster is up and running)
