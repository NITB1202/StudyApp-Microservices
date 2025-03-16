package com.study.apigateway.controllers;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
public class SwaggerLinksController {
    private final RouteLocator routeLocator;

    public SwaggerLinksController(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    @GetMapping
    @ResponseBody
    public Mono<String> getServicesHtml() {
        return routeLocator.getRoutes()
                .filter(route -> !route.getId().contains("api-doc"))
                .map(route -> {
                    String serviceName = route.getId();

                    String displayName = Arrays.stream(serviceName.split("-"))
                            .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                            .collect(Collectors.joining(" "));

                    String swaggerUrl = "/" + serviceName + "/swagger/ui";

                    return "<li><a href='" + swaggerUrl + "' target='_blank'>" + displayName + "</a></li>";
                })
                .collectList()
                .map(list -> {
                    StringBuilder html = new StringBuilder();
                    html.append("<html><head><title>Swagger Services</title>");
                    html.append("<style>body { font-family: Arial; } a { text-decoration: none; }</style>");
                    html.append("</head><body>");
                    html.append("<h1>Available Services</h1><ul>");
                    list.forEach(html::append);
                    html.append("</ul></body></html>");
                    return html.toString();
                });
    }
}