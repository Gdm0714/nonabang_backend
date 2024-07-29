package inje.nonabang.controller;

import inje.nonabang.dto.ApiResponse;
import inje.nonabang.dto.BoardRequest;
import inje.nonabang.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {
    private static final Logger log = LoggerFactory.getLogger(BoardController.class);
    private final BoardService boardService;

    @GetMapping("/board/write")
    public String boardWriteForm(){
        return "boardwrite";
    }
    @Operation(summary = "create post")
    @PostMapping(name = "/board/writepro", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ApiResponse boardWritePro(@ModelAttribute BoardRequest board)throws Exception{

        return ApiResponse.success(boardService.registerImage(board));
    }

    @Operation(summary = "aws s3 파일 업로드")
    @PostMapping("/board/upload")
    public ApiResponse uploadFile(@ModelAttribute BoardRequest request)throws IOException {
        return ApiResponse.success(boardService.registerImage(request));
    }
}