package inje.nonabang.controller;

import inje.nonabang.service.S3ImageService;
import inje.nonabang.service.MemberService; // MemberService를 추가하여 Member 정보를 처리합니다.
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class S3Controller {

    private final S3ImageService s3ImageService;
    private final MemberService memberService; // Member 정보를 처리할 서비스

    @PostMapping(path = "/teams", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPetImage(
            @RequestPart(value = "fileName") String fileName,
            @RequestPart(value = "file", required = false) MultipartFile multipartFile,
            @AuthenticationPrincipal User user
    ) throws IOException {
        // MemberId가 유효한지 확인
        if (user.getUsername() == null) {
            return new ResponseEntity<>("Member ID is required.", HttpStatus.BAD_REQUEST);
        }

        // 파일이 비어있는지 확인
        if (multipartFile == null || multipartFile.isEmpty()) {
            return new ResponseEntity<>("File is empty or missing.", HttpStatus.BAD_REQUEST);
        }

        // 파일 확장자 추출
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            return new ResponseEntity<>("Invalid file name.", HttpStatus.BAD_REQUEST);
        }
        String extend = originalFilename.substring(originalFilename.lastIndexOf("."));


        try {
            // 이미지 업로드
            String url = s3ImageService.upload(fileName, multipartFile, extend);
            log.info("Uploaded image URL: " + url);

            // User 객체에서 ID를 String으로 가져온 뒤 Long으로 변환
            String userIdString = user.getUsername(); // getId() 메서드가 String을 반환한다고 가정
            System.out.println("userIdString = " + userIdString);


          /*  try {

                userId = Long.parseLong(userIdString);

            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                return new ResponseEntity<>("Invalid User ID.", HttpStatus.BAD_REQUEST);
            }*/


            // Image URL을 Member와 함께 저장
            s3ImageService.saveImageUrl(userIdString, fileName, url);

            // 성공 응답 반환
            return new ResponseEntity<>(url, HttpStatus.OK);
        } catch (IOException e) {
            log.error("Error uploading image", e);
            return new ResponseEntity<>("Error uploading image: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/teams/{fileName}")
    public ResponseEntity<byte[]> getPetImage(
            @PathVariable String fileName
    ) throws IOException {
        return s3ImageService.download(fileName);
    }
}
