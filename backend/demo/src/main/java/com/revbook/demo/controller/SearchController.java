package com.revbook.demo.controller;

import com.revbook.demo.dto.SearchRequestDTO;
import com.revbook.demo.dto.SearchResultDTO;
import com.revbook.demo.service.SearchService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping("/search")
    public ResponseEntity<SearchResultDTO> search(@RequestBody SearchRequestDTO searchRequestDTO, HttpSession session) {
        Long sessionUserId = (Long) session.getAttribute("userId");
        if (sessionUserId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String searchTerm = searchRequestDTO.getSearchTerm();

        if (searchTerm == null || searchTerm.isBlank()) {
            return ResponseEntity.badRequest().body(null);
        }

        SearchResultDTO searchResult = searchService.performSearch(searchTerm);
        return ResponseEntity.ok(searchResult);
    }

}
