package com.example.productapi.controller;

import com.example.productapi.dto.request.BotRequest;
import com.example.productapi.dto.response.BotListResponse;
import com.example.productapi.dto.response.BotResponse;
import com.example.productapi.dto.response.CategoryResponse;
import com.example.productapi.service.BotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bots")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Bot", description = "Bot management APIs")
public class BotController {
    
    private final BotService botService;
    
    @GetMapping
    @Operation(summary = "Get all bots", description = "Get all bots with pagination and filtering")
    public ResponseEntity<Page<BotListResponse>> getAllBots(
            @Parameter(description = "Language (vi/en)", example = "vi")
            @RequestParam(defaultValue = "vi") String lang,
            
            @Parameter(description = "Filter by category")
            @RequestParam(required = false) String category,
            
            @Parameter(description = "Search by keyword")
            @RequestParam(required = false) String keyword,
            
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            
            @Parameter(description = "Sort direction (ASC/DESC)")
            @RequestParam(defaultValue = "DESC") String sortDir,

            @Parameter(description = "Filter by tag")
            @RequestParam(required = false) String tag) {
        
        log.info("Getting all bots - lang: {}, category: {}, keyword: {}, page: {}, size: {}", 
                lang, category, keyword, page, size);
        
        Page<BotListResponse> bots = botService.getAllBots(lang, category, keyword, page, size, sortBy, sortDir, tag);
        return ResponseEntity.ok(bots);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get bot by ID", description = "Get a specific bot by its ID")
    public ResponseEntity<BotResponse> getBotById(
            @Parameter(description = "Bot ID", required = true)
            @PathVariable Long id) {
        
        log.info("Getting bot by id: {}", id);
        BotResponse bot = botService.getBotById(id);
        return ResponseEntity.ok(bot);
    }
    
    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get bot by slug", description = "Get a specific bot by its slug")
    public ResponseEntity<BotResponse> getBotBySlug(
            @Parameter(description = "Bot slug", required = true)
            @PathVariable String slug) {
        
        log.info("Getting bot by slug: {}", slug);
        BotResponse bot = botService.getBotBySlug(slug);
        return ResponseEntity.ok(bot);
    }
    
    @GetMapping("/categories")
    @Operation(summary = "Get all categories", description = "Get all unique bot categories/tags")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        log.info("Getting all bot categories");
        List<CategoryResponse> categories = botService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    
    @PostMapping
    @Operation(summary = "Create new bot", description = "Create a new bot")
    public ResponseEntity<BotResponse> createBot(
            @Parameter(description = "Bot details", required = true)
            @Valid @RequestBody BotRequest request) {
        
        log.info("Creating new bot with slug: {}", request.getSlug());
        BotResponse bot = botService.createBot(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(bot);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update bot", description = "Update an existing bot")
    public ResponseEntity<BotResponse> updateBot(
            @Parameter(description = "Bot ID", required = true)
            @PathVariable Long id,
            
            @Parameter(description = "Bot details", required = true)
            @Valid @RequestBody BotRequest request) {
        
        log.info("Updating bot with id: {}", id);
        BotResponse bot = botService.updateBot(id, request);
        return ResponseEntity.ok(bot);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete bot", description = "Delete a bot")
    public ResponseEntity<Void> deleteBot(
            @Parameter(description = "Bot ID", required = true)
            @PathVariable Long id) {
        
        log.info("Deleting bot with id: {}", id);
        botService.deleteBot(id);
        return ResponseEntity.noContent().build();
    }
}