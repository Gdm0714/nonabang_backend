package inje.nonabang.service;

import inje.nonabang.dto.BoardRequest;
import inje.nonabang.dto.BoardResponse;
import inje.nonabang.dto.ImageUploadResponse;
import inje.nonabang.entity.Board;
import inje.nonabang.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final AmazonS3Service amazonS3Service;


    // 이미지를 등록하는 메소드
    public ImageUploadResponse registerImage(BoardRequest request)throws IOException{
        List<String> recipeImageUrls = uploadImagesToS3(request.getImages()); // 이미지를 S3에 업로드하고 URL을 가져옴
        List<String> imageUrls = new ArrayList<>(); // 이미지 URL을 저장할 리스xm
        boardRepository.save(Board.from(request,recipeImageUrls));
        return ImageUploadResponse.from(imageUrls); // 이미지 URL 리스트를 반환

    }

    private List<String> uploadImagesToS3(List<MultipartFile> images) throws IOException {
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile image : images) { // 이미지 리스트를 순회
            String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename(); // 파일 이름을 생성
            String imageUrl = amazonS3Service.uploadImage(image, fileName); // 이미지를 S3에 업로드하고 URL을 가져옴
            imageUrls.add(imageUrl); // URL을 리스트에 추가
        }
        return imageUrls; // URL 리스트를 반환
    }
}
