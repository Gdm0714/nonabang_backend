package inje.nonabang.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import inje.nonabang.entity.Image;
import inje.nonabang.entity.Member;
import inje.nonabang.repository.ImageRepository;
import inje.nonabang.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ImageService{


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final ImageRepository imageRepository;
    private final AmazonS3 amazonS3;
    private final MemberRepository memberRepository;


    private final String DIR_NAME = "pet_picture/";

    public String upload(String fileName, MultipartFile multipartFile, String extend) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
        return upload(fileName, uploadFile, extend);
    }

    private String upload(String fileName, File uploadFile, String extend) {
        String newFileName = DIR_NAME + fileName + extend;
        String uploadImageUrl = putS3(uploadFile, newFileName);
        removeLocalFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private void removeLocalFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("로컬 파일 삭제 성공: {}", targetFile.getName());
        } else {
            log.error("로컬 파일 삭제 실패: {}", targetFile.getName());
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new IllegalArgumentException("업로드된 파일의 이름이 유효하지 않습니다.");
        }

        String newFileName = System.currentTimeMillis() + "_" + originalFileName.replaceAll(" ", "_");
        File convertFile = new File(DIR_NAME + newFileName);
        convertFile.getParentFile().mkdirs();

        try (FileOutputStream fos = new FileOutputStream(convertFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("MultipartFile을 File로 변환하는 중 오류 발생: {}", e.getMessage());
            return Optional.empty();
        }

        return Optional.of(convertFile);
    }

    public ResponseEntity<byte[]> download(String fileName) throws IOException {
        S3Object awsS3Object = amazonS3.getObject(new GetObjectRequest(bucket, DIR_NAME + fileName));
        S3ObjectInputStream s3is = awsS3Object.getObjectContent();
        byte[] bytes = s3is.readAllBytes();

        String downloadedFileName = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", downloadedFileName);
        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

    @Transactional
    public void saveImageUrl(String name, String fileName, String url) {
        log.debug("Saving image URL: id={}, fileName={}, url={}", name, fileName, url);

        Member member = memberRepository.findByMemberName(name)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Image image = Image.builder()
                .url(url)
                .fileName(fileName)
                .member(member)
                .uploadDate(LocalDateTime.now())
                .build();

        imageRepository.save(image);
    }

}