package com.redhat.consulting.sample;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;

public class UrlFixer extends RouteBuilder {

    private static void fixUrlQuery(Exchange exchange) {
        Message message = exchange.getMessage();
        String rawQuery = message.getHeader(Exchange.HTTP_RAW_QUERY, String.class);
        if (rawQuery != null) {
            message.setHeader(Exchange.HTTP_RAW_QUERY, rawQuery.replaceAll("\\?", "&"));
        }
    }

    @Override
    public void configure() throws Exception {
        from("netty-http:proxy://0.0.0.0:8080?bridgeEndpoint=true")
            .routeId("proxy")
            .process(UrlFixer::fixUrlQuery)
            .to("https://echo-api.3scale.net/?bridgeEndpoint=true&disableStreamCache=true");
    }
}
