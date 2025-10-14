package com.example.productapi.repository;

import com.example.productapi.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagsRepository extends JpaRepository<Tags, Long> {
    List<Tags> findAllByCategoryId(Long categoryId);
}
