package com.scheduler.backend.services;

import com.scheduler.backend.dtos.BoardDto;
import com.scheduler.backend.entities.Board;
import com.scheduler.backend.entities.User;
import com.scheduler.backend.repositories.BoardRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BoardService(BoardRepository boardRepository, ModelMapper modelMapper) {
        this.boardRepository = boardRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public BoardDto createBoard(BoardDto boardDTO, User creator) {
        Board board = modelMapper.map(boardDTO, Board.class);
        board.setCreatedAt(LocalDateTime.now());
        board.setName(boardDTO.getName());
        board.setCreator(creator);
        Board savedBoard = boardRepository.save(board);
        return modelMapper.map(savedBoard, BoardDto.class);
    }

    public List<BoardDto> getAllBoardsForCurrentUser(Long currentUserId) {
        List<Board> boards = boardRepository.findByCreatorId(currentUserId);
        return boards.stream()
                .map(board -> modelMapper.map(board, BoardDto.class))
                .collect(Collectors.toList());
    }

    public BoardDto getBoardById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Board not found with id: " + id));
        return modelMapper.map(board, BoardDto.class);
    }
}
