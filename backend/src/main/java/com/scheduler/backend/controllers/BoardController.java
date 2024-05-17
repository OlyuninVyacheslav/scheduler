package com.scheduler.backend.controllers;
import com.scheduler.backend.config.UserAuthenticationProvider;
import com.scheduler.backend.dtos.BoardDto;
import com.scheduler.backend.dtos.UserDto;
import com.scheduler.backend.entities.User;
import com.scheduler.backend.repositories.BoardRepository;
import com.scheduler.backend.repositories.UserRepository;
import com.scheduler.backend.services.BoardService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
public class BoardController {
    private final UserRepository userRepository;
    private final BoardService boardService;

    private final BoardRepository boardRepository;
    private final UserAuthenticationProvider userAuthenticationProvider;

    @Autowired
    public BoardController(UserRepository userRepository, BoardService boardService, BoardRepository boardRepository, UserAuthenticationProvider userAuthenticationProvider) {
        this.userRepository = userRepository;
        this.boardService = boardService;
        this.boardRepository = boardRepository;
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDto> getBoardById(@PathVariable Long id) {
        BoardDto boardDTO = boardService.getBoardById(id);
        return ResponseEntity.ok(boardDTO);
    }

    @PostMapping("/create")
    public ResponseEntity<BoardDto> createBoard(@RequestBody BoardDto boardDTO, @RequestHeader("Authorization") String token) {
        try{
            String jwt = token.substring(7);
            Authentication authentication = userAuthenticationProvider.validateToken(jwt);
            Long creatorId = ((UserDto) authentication.getPrincipal()).getId();
            User creator = userRepository.findById(creatorId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + creatorId));
            BoardDto createdBoard = boardService.createBoard(boardDTO, creator);
            //System.out.println("CreatedBoard: "+ createdBoard.getName() + " " + createdBoard.getCreator());
            return new ResponseEntity<>(createdBoard, HttpStatus.CREATED);
        }catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch( Exception e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }


//    @GetMapping
//    public ResponseEntity<List<BoardDto>> getAllBoards() {
//        List<BoardDto> boards = boardService.getAllBoards();
//        return ResponseEntity.ok(boards);
//    }

    @GetMapping
    public ResponseEntity<List<BoardDto>> getAllBoards(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.substring(7);
            Authentication authentication = userAuthenticationProvider.validateToken(jwt);
            Long currentUserId = ((UserDto) authentication.getPrincipal()).getId();
            List<BoardDto> boards = boardService.getAllBoardsForCurrentUser(currentUserId);
            return ResponseEntity.ok(boards);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // Другие методы контроллера, например, для обновления, удаления и т.д.
}
