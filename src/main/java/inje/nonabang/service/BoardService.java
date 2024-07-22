package inje.nonabang.service;

import inje.nonabang.dto.BoardRequest;
import inje.nonabang.dto.BoardResponse;
import inje.nonabang.entity.Board;
import inje.nonabang.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    public String write(BoardRequest board){
        boardRepository.save(Board.from(board));

        return "success";
    }


}
