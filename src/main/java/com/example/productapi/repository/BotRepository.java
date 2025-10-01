package com.example.productapi.repository;

import com.example.productapi.entity.Bot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BotRepository extends JpaRepository<Bot, Long> {
    
    Optional<Bot> findBySlug(String slug);
    
    Optional<Bot> findByExternalKey(String externalKey);
    
    @Query("SELECT b FROM Bot b LEFT JOIN FETCH b.features LEFT JOIN FETCH b.strengths " +
           "LEFT JOIN FETCH b.weaknesses LEFT JOIN FETCH b.targetUsers LEFT JOIN FETCH b.pricingPlans " +
           "WHERE b.id = :id")
    Optional<Bot> findByIdWithDetails(@Param("id") Long id);
    
    @Query("SELECT b FROM Bot b LEFT JOIN FETCH b.features LEFT JOIN FETCH b.strengths " +
           "LEFT JOIN FETCH b.weaknesses LEFT JOIN FETCH b.targetUsers LEFT JOIN FETCH b.pricingPlans " +
           "WHERE b.slug = :slug")
    Optional<Bot> findBySlugWithDetails(@Param("slug") String slug);
    
    @Query(value = "SELECT * FROM bots b WHERE :tag = ANY(b.tags)", nativeQuery = true)
    Page<Bot> findByTag(@Param("tag") String tag, Pageable pageable);
    
    @Query("SELECT b FROM Bot b WHERE " +
           "(LOWER(b.nameVi) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.nameEn) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.summaryVi) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.summaryEn) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Bot> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query(value = "SELECT DISTINCT unnest(tags) as tag FROM bots ORDER BY tag", nativeQuery = true)
    List<String> findAllUniqueTags();
    
    boolean existsBySlug(String slug);
    
    boolean existsByExternalKey(String externalKey);
}