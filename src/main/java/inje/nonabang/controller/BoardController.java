package inje.nonabang.controller;

import inje.nonabang.dto.ApiResponse;
import inje.nonabang.dto.BoardRequest;
import inje.nonabang.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/board/writepro")
    public ApiResponse boardWritePro(@RequestBody BoardRequest board){

        return ApiResponse.success(boardService.write(board));

    }
}