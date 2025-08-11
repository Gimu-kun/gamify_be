package com.example.gamify_be.Utils;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class FirebaseUtil {

    public String uploadImage(MultipartFile image, String fileName) throws IOException {
        //Khởi tạo bucket (xxxx.appspot.com)
        Bucket bucket = StorageClient.getInstance().bucket();

        //Khai báo thông tin file
        String contentType = image.getContentType();
        String downloadToken = UUID.randomUUID().toString();

        //Tạo metadata
        Map<String,String> metadata = new HashMap<>();
        metadata.put("firebaseStorageDownloadTokens",downloadToken);

        //Xây lại BlobInfo của file
        BlobInfo blobInfo = BlobInfo.newBuilder(bucket.getName(),fileName)
                .setContentType(contentType)
                .setMetadata(metadata)
                .build();

        //Lấy thông tin Storage
        Storage storage = StorageClient.getInstance().bucket().getStorage();

        //Upload lên Storage
        storage.create(blobInfo,image.getBytes());

        //Lấy url của ảnh
        return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media&token=%s",
                bucket.getName(),
                fileName.replace("/","%2F"),
                downloadToken);
    };
}
