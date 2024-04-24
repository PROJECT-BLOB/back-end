package com.codeit.blob.global.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    private static final Set<String> VALID_EXTENSIONS = new HashSet<>();
    static {
        VALID_EXTENSIONS.add(".jpg");
        VALID_EXTENSIONS.add(".jpeg");
        VALID_EXTENSIONS.add(".png");
    }

    public String uploadFile(MultipartFile file) {
        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket + "/image", fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (AmazonS3Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "S3 에러로 인해 파일 업로드에 실패했습니다.");
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "IO 에러로 인해 파일 업로드에 실패했습니다.");
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
        }

        return amazonS3.getUrl(bucket + "/image",fileName).toString();
    }

    public List<String> uploadFiles(List<MultipartFile> files) {
        List<String> imgUrls = new ArrayList<>();

        for(MultipartFile file : files){
            String url = uploadFile(file);
            imgUrls.add(url);
        }

        return imgUrls;
    }

    public String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        if (lastIndex == -1 || lastIndex == fileName.length() - 1) {
            throw new IllegalArgumentException("잘못된 형식의 파일 (" + fileName + ") 입니다.");
        }

        String extension = fileName.substring(lastIndex);
        if (!VALID_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("잘못된 형식의 파일 (" + fileName + ") 입니다.");
        }

        return extension;
    }

}
