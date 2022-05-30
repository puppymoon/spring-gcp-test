package com.example.springgcptest.service;

import com.google.cloud.ReadChannel;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;

import static com.google.cloud.storage.Acl.Role.READER;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Service
public class CloudStorageService {

    @Value("${google.storage.bucket}")
    private String bucketName;

    private static Storage storage = StorageOptions.getDefaultInstance().getService();

    private static final String encryptionKey = "xxxxxxxxtest";

    public String upload(MultipartFile file) throws IOException {

        String fileName = file.getName();
        BlobId blobId = BlobId.of(bucketName, fileName);
        byte[] content = file.getBytes();
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/plain").build();
//        try (WriteChannel writer = storage.writer(blobInfo, Storage.BlobWriteOption.encryptionKey(encryptionKey))) {
        try (WriteChannel writer = storage.writer(blobInfo)) {
            writer.write(ByteBuffer.wrap(content, 0, content.length));
        }
        return bucketName + "/" + fileName;
    }

    public void download(String fileName, OutputStream out) throws IOException {

        BlobId blobId = BlobId.of(bucketName, fileName);
//        try (ReadChannel reader = storage.reader(blobId, Storage.BlobSourceOption.decryptionKey(encryptionKey)); WritableByteChannel channel = Channels.newChannel(out)) {
        try (ReadChannel reader = storage.reader(blobId); WritableByteChannel channel = Channels.newChannel(out)) {
            ByteBuffer bytes = ByteBuffer.allocate(64 * 1024);
            while (reader.read(bytes) > 0) {
                bytes.flip();
                channel.write(bytes);
                bytes.clear();
            }
        }
    }

}