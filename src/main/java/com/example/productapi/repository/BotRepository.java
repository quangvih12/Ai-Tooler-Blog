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
    
    @Query("SELECT DISTINCT b FROM Bot b " +
           "LEFT JOIN FETCH b.features " +
           "WHERE b.id = :id")
    Optional<Bot> findByIdWithFeatures(@Param("id") Long id);

    @Query("SELECT DISTINCT b FROM Bot b " +
           "LEFT JOIN FETCH b.strengths " +
           "WHERE b.id = :id")
    Optional<Bot> findByIdWithStrengths(@Param("id") Long id);

    @Query("SELECT DISTINCT b FROM Bot b " +
           "LEFT JOIN FETCH b.weaknesses " +
           "WHERE b.id = :id")
    Optional<Bot> findByIdWithWeaknesses(@Param("id") Long id);

    @Query("SELECT DISTINCT b FROM Bot b " +
           "LEFT JOIN FETCH b.targetUsers " +
           "WHERE b.id = :id")
    Optional<Bot> findByIdWithTargetUsers(@Param("id") Long id);

    @Query("SELECT DISTINCT b FROM Bot b " +
           "LEFT JOIN FETCH b.pricingPlans " +
           "WHERE b.id = :id")
    Optional<Bot> findByIdWithPricingPlans(@Param("id") Long id);

    default Optional<Bot> findByIdWithDetails(@Param("id") Long id) {
        return findByIdWithFeatures(id)
            .map(bot -> {
                findByIdWithStrengths(id);
                findByIdWithWeaknesses(id);
                findByIdWithTargetUsers(id);
                findByIdWithPricingPlans(id);
                return bot;
            });
    }

    @Query("SELECT DISTINCT b FROM Bot b " +
           "LEFT JOIN FETCH b.features " +
           "WHERE b.slug = :slug")
    Optional<Bot> findBySlugWithFeatures(@Param("slug") String slug);

    @Query("SELECT DISTINCT b FROM Bot b " +
           "LEFT JOIN FETCH b.strengths " +
           "WHERE b.slug = :slug")
    Optional<Bot> findBySlugWithStrengths(@Param("slug") String slug);

    @Query("SELECT DISTINCT b FROM Bot b " +
           "LEFT JOIN FETCH b.weaknesses " +
           "WHERE b.slug = :slug")
    Optional<Bot> findBySlugWithWeaknesses(@Param("slug") String slug);

    @Query("SELECT DISTINCT b FROM Bot b " +
           "LEFT JOIN FETCH b.targetUsers " +
           "WHERE b.slug = :slug")
    Optional<Bot> findBySlugWithTargetUsers(@Param("slug") String slug);

    @Query("SELECT DISTINCT b FROM Bot b " +
           "LEFT JOIN FETCH b.pricingPlans " +
           "WHERE b.slug = :slug")
    Optional<Bot> findBySlugWithPricingPlans(@Param("slug") String slug);

    default Optional<Bot> findBySlugWithDetails(@Param("slug") String slug) {
        return findBySlugWithFeatures(slug)
            .map(bot -> {
                findBySlugWithStrengths(slug);
                findBySlugWithWeaknesses(slug);
                findBySlugWithTargetUsers(slug);
                findBySlugWithPricingPlans(slug);
                return bot;
            });
    }

    @Query(value = "SELECT * FROM bots b WHERE b.tags && CAST(:tags AS text[]) ORDER BY b.created_at DESC", nativeQuery = true)
    Page<Bot> findByTags(@Param("tags") String[] tags, Pageable pageable);

    @Query(value = "SELECT * FROM bots b WHERE " +
            "(unaccent(LOWER(b.name_vi)) LIKE unaccent(LOWER('%' || :keyword || '%')) OR " +
            "unaccent(LOWER(b.name_en)) LIKE unaccent(LOWER('%' || :keyword || '%')))", 
            countQuery = "SELECT count(*) FROM bots b WHERE " +
            "(unaccent(LOWER(b.name_vi)) LIKE unaccent(LOWER('%' || :keyword || '%')) OR " +
            "unaccent(LOWER(b.name_en)) LIKE unaccent(LOWER('%' || :keyword || '%')))",
            nativeQuery = true)
    Page<Bot> searchByKeywordUnicodeInsensitive(@Param("keyword") String keyword, Pageable pageable);
    
    @Query(value = "SELECT * FROM bots b WHERE " +
            "(unaccent(LOWER(b.name_vi)) LIKE unaccent(LOWER('%' || :keyword || '%')) OR " +
            "unaccent(LOWER(b.name_en)) LIKE unaccent(LOWER('%' || :keyword || '%'))) " +
            "AND b.tags && CAST(:tags AS text[]) ORDER BY b.created_at DESC", 
            countQuery = "SELECT count(*) FROM bots b WHERE " +
            "(unaccent(LOWER(b.name_vi)) LIKE unaccent(LOWER('%' || :keyword || '%')) OR " +
            "unaccent(LOWER(b.name_en)) LIKE unaccent(LOWER('%' || :keyword || '%'))) " +
            "AND b.tags && CAST(:tags AS text[])",
            nativeQuery = true)
    Page<Bot> searchByKeywordAndTags(@Param("keyword") String keyword, @Param("tags") String[] tags, Pageable pageable);

    @Query(value = "SELECT DISTINCT unnest(tags) as tag FROM bots ORDER BY tag", nativeQuery = true)
    List<String> findAllUniqueTags();
    
    boolean existsBySlug(String slug);
    
    boolean existsByExternalKey(String externalKey);
}