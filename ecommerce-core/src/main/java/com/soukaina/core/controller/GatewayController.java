package com.soukaina.core.controller;

import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/gateway")
public class GatewayController {

    private final ReactiveOAuth2AuthorizedClientManager authorizedClientManager;

    public GatewayController(ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
        this.authorizedClientManager = authorizedClientManager;
    }

    @GetMapping("/ecommerce")
    public Mono<String> getEcommerceApplication() {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("ecommerce-client")
                .principal("ecommerce-client") // Must match client ID
                .build();

        return authorizedClientManager.authorize(authorizeRequest)
                .map(OAuth2AuthorizedClient::getAccessToken)
                .map(token -> "Access Token: " + token.getTokenValue())
                .defaultIfEmpty("Error: Unable to retrieve access token");
    }
}
