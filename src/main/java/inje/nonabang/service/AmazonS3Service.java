package inje.nonabang.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


@Service
@RequiredArgsConstructor
@Slf4j
public class AmazonS3Service {
    private final AmazonS3Client s3Client;


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 이미지를 S3에 업로드하는 메소드
    public String uploadImage(MultipartFile image, String fileName) throws IOException {
        log.info(image.getOriginalFilename());
        try (InputStream inputStream = image.getInputStream()) { // 이미지를 InputStream으로 변환
            ObjectMetadata metadata = new ObjectMetadata(); // 메타데이터 객체 생성
            metadata.setContentLength(image.getSize()); // 메타데이터에 이미지 크기 설정
            metadata.setContentType(image.getContentType()); // 메타데이터에 이미지 타입 설정

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, inputStream, metadata); // S3에 업로드할 객체 생성

            putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead); // 객체에 대한 public read 권한 설정

            s3Client.putObject(putObjectRequest); // 객체를 S3에 업로드
            return s3Client.getUrl(bucket, fileName).toString(); // 업로드된 객체의 URL을 반환
        }
    }
}
