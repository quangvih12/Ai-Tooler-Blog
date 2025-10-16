package com.example.productapi.repository;

import com.example.productapi.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    
    Optional<Blog> findBySlug(String slug);
    
    @Query("SELECT b FROM Blog b WHERE b.status = :status")
    Page<Blog> findByStatus(@Param("status") String status, Pageable pageable);
    
    @Query(value = "SELECT * FROM blogs b WHERE :tag = ANY(b.tags) AND b.status = :status", 
           countQuery = "SELECT count(*) FROM blogs b WHERE :tag = ANY(b.tags) AND b.status = :status",
           nativeQuery = true)
    Page<Blog> findByTagAndStatus(@Param("tag") String tag, @Param("status") String status, Pageable pageable);
    
    @Query("SELECT b FROM Blog b WHERE " +
           "(LOWER(b.titleVi) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.titleEn) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.excerptVi) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.excerptEn) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND b.status = :status")
    Page<Blog> searchByKeyword(@Param("keyword") String keyword, @Param("status") String status, Pageable pageable);

    @Query(value = "SELECT * FROM blogs b WHERE " +
           "(unaccent(LOWER(b.title_vi)) LIKE unaccent(LOWER('%' || :keyword || '%')) OR " +
           "unaccent(LOWER(b.title_en)) LIKE unaccent(LOWER('%' || :keyword || '%')) OR " +
           "unaccent(LOWER(b.excerpt_vi)) LIKE unaccent(LOWER('%' || :keyword || '%')) OR " +
           "unaccent(LOWER(b.excerpt_en)) LIKE unaccent(LOWER('%' || :keyword || '%'))) " +
           "AND b.status = :status", 
           countQuery = "SELECT count(*) FROM blogs b WHERE " +
           "(unaccent(LOWER(b.title_vi)) LIKE unaccent(LOWER('%' || :keyword || '%')) OR " +
           "unaccent(LOWER(b.title_en)) LIKE unaccent(LOWER('%' || :keyword || '%')) OR " +
           "unaccent(LOWER(b.excerpt_vi)) LIKE unaccent(LOWER('%' || :keyword || '%')) OR " +
           "unaccent(LOWER(b.excerpt_en)) LIKE unaccent(LOWER('%' || :keyword || '%'))) " +
           "AND b.status = :status",
           nativeQuery = true)
    Page<Blog> searchByKeywordUnicodeInsensitive(@Param("keyword") String keyword, @Param("status") String status, Pageable pageable);
    
    @Query(value = "SELECT DISTINCT unnest(tags) as tag FROM blogs WHERE status = :status", nativeQuery = true)
    List<String> findAllUniqueTags(@Param("status") String status);
    
    boolean existsBySlug(String slug);
}