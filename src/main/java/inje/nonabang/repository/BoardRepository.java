package inje.nonabang.repository;

import inje.nonabang.dto.BoardRequest;
import inje.nonabang.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Integer> {

    Optional<Board> save(BoardRequest board);
}
