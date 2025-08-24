package com.complete.api.gateway.handler;

public class GlobalExceptionHandlerV2 {
    //implements ErrorWebExceptionHandler {

    /*private List<HttpMessageReader<?>> messageReaders = Collections.emptyList();
    private List<HttpMessageWriter<?>> messageWriters = Collections.emptyList();
    private List<ViewResolver> viewResolvers = Collections.emptyList();

    public void setMessageReaders(List<HttpMessageReader<?>> messageReaders) {
        this.messageReaders = messageReaders;
    }

    public void setMessageWriters(List<HttpMessageWriter<?>> messageWriters) {
        this.messageWriters = messageWriters;
    }

    public void setViewResolvers(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status;
        String message = "Internal Server Error";

        if (ex instanceof NotFoundException) {
            status = HttpStatus.NOT_FOUND;
            message = "Service not found";
        } else if (ex instanceof io.netty.channel.ConnectTimeoutException) {
            status = HttpStatus.GATEWAY_TIMEOUT;
            message = "Connection timeout";
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        Map<String, String> errorResponse = Map.of(
                "error", status.getReasonPhrase(),
                "message", message,
                "status", String.valueOf(status.value())
        );

        ServerRequest request = ServerRequest.create(exchange, this.messageReaders);

        return RouterFunctions.route(RequestPredicates.all(), req -> ServerResponse.status(HttpStatusCode.valueOf(status.value()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(errorResponse)))
                .route(request)
                .switchIfEmpty(Mono.error(ex))
                .flatMap(handler -> handler.handle(request))
                .flatMap(response -> response.writeTo(exchange, new ResponseContext()));
    }

    private class ResponseContext implements ServerResponse.Context {
        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return GlobalExceptionHandler.this.messageWriters;
        }

        @Override
        public List<ViewResolver> viewResolvers() {
            return GlobalExceptionHandler.this.viewResolvers;
        }
    }*/
}