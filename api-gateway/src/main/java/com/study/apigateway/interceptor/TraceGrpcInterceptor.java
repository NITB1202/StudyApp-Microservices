package com.study.apigateway.interceptor;

import brave.Tracer;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TraceGrpcInterceptor implements ServerInterceptor {
    private Tracer tracer;

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String traceId = headers.get(Metadata.Key.of("traceId", Metadata.ASCII_STRING_MARSHALLER));
        if (traceId != null) {
            tracer.currentSpan().context().traceId();
        }

        return next.startCall(call, headers);
    }
}
