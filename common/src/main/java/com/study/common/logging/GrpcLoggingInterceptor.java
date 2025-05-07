package com.study.common.logging;

import io.grpc.*;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.slf4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@GrpcGlobalServerInterceptor
public class GrpcLoggingInterceptor implements ServerInterceptor {
    private static final Logger log = LoggerFactory.getLogger("GrpcLogger");

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);

        String methodName = call.getMethodDescriptor().getFullMethodName();
        long start = System.currentTimeMillis();

        log.info("→ Incoming gRPC: {}", methodName);

        ServerCall.Listener<ReqT> listener = next.startCall(call, headers);

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(listener) {
            @Override
            public void onComplete() {
                long duration = System.currentTimeMillis() - start;
                log.info("✓ Completed gRPC: {} in {} ms", methodName, duration);
                MDC.clear();
                super.onComplete();
            }

            @Override
            public void onCancel() {
                log.warn("✗ Canceled gRPC: {}", methodName);
                MDC.clear();
                super.onCancel();
            }
        };
    }
}
