package com.study.chatservice.grpc;

import com.google.protobuf.ByteString;
import com.study.documentservice.grpc.DocumentServiceGrpc;
import com.study.documentservice.grpc.UploadImageRequest;
import com.study.documentservice.grpc.UploadImageResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class DocumentServiceGrpcClient {
    @GrpcClient("document-service")
    private DocumentServiceGrpc.DocumentServiceBlockingStub blockingStub;

    public UploadImageResponse uploadImage(String folderPath, String publicId, byte[] file) {
        ByteString byteString = ByteString.copyFrom(file);

        UploadImageRequest request = UploadImageRequest.newBuilder()
                .setFolderPath(folderPath)
                .setPublicId(publicId)
                .setFile(byteString)
                .build();

        return blockingStub.uploadImage(request);
    }
}
