package com.example.productapi.controller;

import com.example.productapi.dto.request.BlogRequest;
import com.example.productapi.dto.response.BlogListResponse;
import com.example.productapi.dto.response.BlogResponse;
import com.example.productapi.service.BlogService;
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
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Blog", description = "Blog management APIs")
public class BlogController {
    
    private final BlogService blogService;
    
    @GetMapping
    @Operation(summary = "Get all blogs", description = "Get all blogs with pagination and filtering")
    public ResponseEntity<Page<BlogListResponse>> getAllBlogs(
            @Parameter(description = "Language (vi/en)", example = "vi")
            @RequestParam(defaultValue = "vi") String lang,
            
            @Parameter(description = "Filter by category/tag")
            @RequestParam(required = false) String category,
            
            @Parameter(description = "Search by keyword")
            @RequestParam(required = false) String keyword,
            
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "publishedAt") String sortBy,
            
            @Parameter(description = "Sort direction (ASC/DESC)")
            @RequestParam(defaultValue = "DESC") String sortDir,
            @Parameter(description = "Status (active/deactive/draft)")
            @RequestParam(defaultValue = "active") String status) {
        
        log.info("Getting all blogs - lang: {}, category: {}, keyword: {}, page: {}, size: {}", 
                lang, category, keyword, page, size);
        
        Page<BlogListResponse> blogs = blogService.getAllBlogs(lang, category, keyword, page, size, sortBy, sortDir, status);
        return ResponseEntity.ok(blogs);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get blog by ID", description = "Get a specific blog by its ID")
    public ResponseEntity<BlogResponse> getBlogById(
            @Parameter(description = "Blog ID", required = true)
            @PathVariable Long id,
            
            @Parameter(description = "Language (vi/en)", example = "vi")
            @RequestParam(defaultValue = "vi") String lang) {
        
        log.info("Getting blog by id: {}, lang: {}", id, lang);
        BlogResponse blog = blogService.getBlogById(id, lang);
        return ResponseEntity.ok(blog);
    }
    
    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get blog by slug", description = "Get a specific blog by its slug")
    public ResponseEntity<BlogResponse> getBlogBySlug(
            @Parameter(description = "Blog slug", required = true)
            @PathVariable String slug,
            
            @Parameter(description = "Language (vi/en)", example = "vi")
            @RequestParam(defaultValue = "vi") String lang) {
        
        log.info("Getting blog by slug: {}, lang: {}", slug, lang);
        BlogResponse blog = blogService.getBlogBySlug(slug, lang);
        return ResponseEntity.ok(blog);
    }
    
    @GetMapping("/categories")
    @Operation(summary = "Get all categories", description = "Get all unique blog categories/tags")
    public ResponseEntity<List<String>> getAllCategories() {
        log.info("Getting all blog categories");
        List<String> categories = blogService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    
    @PostMapping
    @Operation(summary = "Create new blog", description = "Create a new blog post")
    public ResponseEntity<BlogResponse> createBlog(
            @Parameter(description = "Blog details", required = true)
            @Valid @RequestBody BlogRequest request) {
        
        log.info("Creating new blog with slug: {}", request.getSlug());
        BlogResponse blog = blogService.createBlog(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(blog);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update blog", description = "Update an existing blog post")
    public ResponseEntity<BlogResponse> updateBlog(
            @Parameter(description = "Blog ID", required = true)
            @PathVariable Long id,
            
            @Parameter(description = "Blog details", required = true)
            @Valid @RequestBody BlogRequest request) {
        
        log.info("Updating blog with id: {}", id);
        BlogResponse blog = blogService.updateBlog(id, request);
        return ResponseEntity.ok(blog);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete blog", description = "Delete a blog post")
    public ResponseEntity<Void> deleteBlog(
            @Parameter(description = "Blog ID", required = true)
            @PathVariable Long id) {
        
        log.info("Deleting blog with id: {}", id);
        blogService.deleteBlog(id);
        return ResponseEntity.noContent().build();
    }
}