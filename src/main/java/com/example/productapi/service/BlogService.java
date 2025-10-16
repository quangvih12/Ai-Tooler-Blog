package com.example.productapi.service;

import com.example.productapi.dto.request.BlogRequest;
import com.example.productapi.dto.response.BlogListResponse;
import com.example.productapi.dto.response.BlogResponse;
import com.example.productapi.entity.Blog;
import com.example.productapi.exception.BadRequestException;
import com.example.productapi.exception.ResourceNotFoundException;
import com.example.productapi.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BlogService {
    
    private final BlogRepository blogRepository;
    
    public Page<BlogListResponse> getAllBlogs(String lang, String category, String keyword, 
                                              int page, int size, String sortBy, String sortDir, String status) {
        Page<Blog> blogs;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            // For Unicode-insensitive search, we'll use unsorted pageable and handle sorting manually
            Pageable unsortedPageable = PageRequest.of(page, size);
            blogs = blogRepository.searchByKeywordUnicodeInsensitive(keyword, status, unsortedPageable);
        } else if (category != null && !category.trim().isEmpty()) {
            // For native query, convert camelCase field names to snake_case
            String adjustedSortBy = sortBy;
            if ("publishedAt".equals(sortBy)) {
                adjustedSortBy = "published_at";
            } else if ("createdAt".equals(sortBy)) {
                adjustedSortBy = "created_at";
            } else if ("updatedAt".equals(sortBy)) {
                adjustedSortBy = "updated_at";
            }
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), adjustedSortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            blogs = blogRepository.findByTagAndStatus(category, status, pageable);
        } else {
            // For JPQL query, use original field names
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            blogs = blogRepository.findByStatus(status, pageable);
        }
        
        return blogs.map(blog -> mapToBlogListResponse(blog, lang));
    }
    
    public BlogResponse getBlogById(Long id, String lang) {
        Blog blog = blogRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));
        
        return mapToBlogResponse(blog);
    }
    
    public BlogResponse getBlogBySlug(String slug, String lang) {
        Blog blog = blogRepository.findBySlug(slug)
            .orElseThrow(() -> new ResourceNotFoundException("Blog not found with slug: " + slug));
        
        return mapToBlogResponse(blog);
    }
    
    public BlogResponse createBlog(BlogRequest request) {
        if (blogRepository.existsBySlug(request.getSlug())) {
            throw new BadRequestException("Blog with slug '" + request.getSlug() + "' already exists");
        }
        
        Blog blog = new Blog();
        updateBlogFromRequest(blog, request);
        
        Blog savedBlog = blogRepository.save(blog);
        log.info("Created new blog with slug: {}", savedBlog.getSlug());
        
        return mapToBlogResponse(savedBlog);
    }
    
    public BlogResponse updateBlog(Long id, BlogRequest request) {
        Blog blog = blogRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));
        
        // Check if slug is being changed and if new slug already exists
        if (!blog.getSlug().equals(request.getSlug()) && 
            blogRepository.existsBySlug(request.getSlug())) {
            throw new BadRequestException("Blog with slug '" + request.getSlug() + "' already exists");
        }
        
        updateBlogFromRequest(blog, request);
        
        Blog updatedBlog = blogRepository.save(blog);
        log.info("Updated blog with id: {}", updatedBlog.getId());
        
        return mapToBlogResponse(updatedBlog);
    }
    
    public void deleteBlog(Long id) {
        if (!blogRepository.existsById(id)) {
            throw new ResourceNotFoundException("Blog not found with id: " + id);
        }
        
        blogRepository.deleteById(id);
        log.info("Deleted blog with id: {}", id);
    }
    
    public List<String> getAllCategories() {
        return blogRepository.findAllUniqueTags("active");
    }
    
    private void updateBlogFromRequest(Blog blog, BlogRequest request) {
        blog.setSlug(request.getSlug());
        blog.setTitleVi(request.getTitleVi());
        blog.setTitleEn(request.getTitleEn());
        
        if (request.getTags() != null) {
            blog.setTags(request.getTags().toArray(new String[0]));
        }
        
        blog.setImage(request.getImage());
        blog.setExcerptVi(request.getExcerptVi());
        blog.setExcerptEn(request.getExcerptEn());
        
        if (request.getContentVi() != null) {
            blog.setContentViRaw(request.getContentVi().getRaw());
            blog.setContentViHtml(request.getContentVi().getHtml());
            blog.setContentViText(request.getContentVi().getText());
        }
        
        if (request.getContentEn() != null) {
            blog.setContentEnRaw(request.getContentEn().getRaw());
            blog.setContentEnHtml(request.getContentEn().getHtml());
            blog.setContentEnText(request.getContentEn().getText());
        }
        
        blog.setStatus(request.getStatus());
        
        // Set publishedAt only when status is active
        if ("active".equalsIgnoreCase(request.getStatus())) {
            // Only set publishedAt if it's not already set (for new blogs or first time publishing)
            if (blog.getPublishedAt() == null) {
                blog.setPublishedAt(Instant.now());
            }
        }
        
        blog.setSource(request.getSource());
        blog.setSourceUrl(request.getSourceUrl());
    }
    
    private BlogListResponse mapToBlogListResponse(Blog blog, String lang) {
        return BlogListResponse.builder()
            .id(blog.getId())
            .slug(blog.getSlug())
            .title("vi".equals(lang) ? blog.getTitleVi() : blog.getTitleEn())
            .tags(blog.getTags() != null ? Arrays.asList(blog.getTags()) : List.of())
            .image(blog.getImage())
            .excerpt("vi".equals(lang) ? blog.getExcerptVi() : blog.getExcerptEn())
            .status(blog.getStatus())
            .publishedAt(blog.getPublishedAt())
            .createdAt(blog.getCreatedAt())
            .content_raw("vi".equals(lang) ? blog.getContentEnRaw() : blog.getContentEnRaw())
            .content_html("vi".equals(lang) ? blog.getContentEnHtml() : blog.getContentEnHtml())
            .content_text("vi".equals(lang) ? blog.getContentEnText() : blog.getContentEnText())
            .build();
    }
    
    private BlogResponse mapToBlogResponse(Blog blog) {
        return BlogResponse.builder()
            .id(blog.getId())
            .externalId(blog.getExternalId())
            .slug(blog.getSlug())
            .title(BlogResponse.MultiLangText.builder()
                .vi(blog.getTitleVi())
                .en(blog.getTitleEn())
                .build())
            .tags(blog.getTags() != null ? Arrays.asList(blog.getTags()) : List.of())
            .image(blog.getImage())
            .excerpt(BlogResponse.MultiLangText.builder()
                .vi(blog.getExcerptVi())
                .en(blog.getExcerptEn())
                .build())
            .content(BlogResponse.MultiLangContent.builder()
                .vi(BlogResponse.ContentDetail.builder()
                    .raw(blog.getContentViRaw())
                    .html(blog.getContentViHtml())
                    .text(blog.getContentViText())
                    .build())
                .en(BlogResponse.ContentDetail.builder()
                    .raw(blog.getContentEnRaw())
                    .html(blog.getContentEnHtml())
                    .text(blog.getContentEnText())
                    .build())
                .build())
            .status(blog.getStatus())
            .publishedAt(blog.getPublishedAt())
            .source(blog.getSource())
            .sourceUrl(blog.getSourceUrl())
            .createdAt(blog.getCreatedAt())
            .updatedAt(blog.getUpdatedAt())
            .build();
    }
}