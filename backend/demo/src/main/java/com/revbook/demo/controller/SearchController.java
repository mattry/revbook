package com.revbook.demo.controller;

import com.revbook.demo.dto.SearchRequestDTO;
import com.revbook.demo.dto.SearchResultDTO;
import com.revbook.demo.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping("/search")
    public ResponseEntity<SearchResultDTO> search(@RequestBody SearchRequestDTO searchRequestDTO) {
        String searchTerm = searchRequestDTO.getSearchTerm();

        if (searchTerm == null || searchTerm.isBlank()) {
            return ResponseEntity.badRequest().body(null);
        }

        SearchResultDTO searchResult = searchService.performSearch(searchTerm);
        return ResponseEntity.ok(searchResult);
    }

}
