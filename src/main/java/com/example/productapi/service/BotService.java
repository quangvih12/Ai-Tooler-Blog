package com.example.productapi.service;

import com.example.productapi.dto.request.BotRequest;
import com.example.productapi.dto.response.BotListResponse;
import com.example.productapi.dto.response.BotResponse;
import com.example.productapi.dto.response.CategoryResponse;
import com.example.productapi.entity.*;
import com.example.productapi.exception.BadRequestException;
import com.example.productapi.exception.ResourceNotFoundException;
import com.example.productapi.repository.BotRepository;
import com.example.productapi.repository.CategoryRepository;
import com.example.productapi.repository.TagsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BotService {
    
    private final BotRepository botRepository;
    private final CategoryRepository categoryRepository;
    private final TagsRepository tagsRepository;

    public Page<BotListResponse> getAllBots(String lang, String category, String keyword,
                                            int page, int size, String sortBy, String sortDir, String tag) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Bot> bots;

        // ===== CASE 1: keyword != null && tag != null =====
        if (keyword != null && !keyword.trim().isEmpty() && tag != null && !tag.trim().isEmpty()) {
            Pageable unsortedPageable = PageRequest.of(page, size);
            bots = botRepository.searchByKeywordAndTags(keyword, new String[]{tag}, unsortedPageable);
        }
        
        // ===== CASE 2: keyword only =====
        else if (keyword != null && !keyword.trim().isEmpty()) {
            Pageable unsortedPageable = PageRequest.of(page, size);
            bots = botRepository.searchByKeywordUnicodeInsensitive(keyword, unsortedPageable);
        }

        // ===== CASE 3-6:category & tag =====
        else {
            boolean hasCategory = category != null && !category.trim().isEmpty();
            boolean hasTag = tag != null && !tag.trim().isEmpty();

            // CASE 1: category = null, tag = null
            if (!hasCategory && !hasTag) {
                bots = botRepository.findAll(pageable);
            }
            // CASE 2: has category, no tag
            else if (hasCategory && !hasTag) {
                Optional<Category> cate = Optional.empty();

                if (category.matches("\\d+")) {
                    cate = categoryRepository.findById(Long.parseLong(category));
                }

                if (cate.isPresent()) {
                    List<Tags> tags = tagsRepository.findAllByCategoryId(cate.get().getId());
                    List<String> tagNames = tags.stream()
                            .map(Tags::getName)
                            .collect(Collectors.toList());

                    Pageable unsortedPageable = PageRequest.of(page, size);
                    bots = botRepository.findByTags(tagNames.toArray(new String[0]), unsortedPageable);
                } else {
                    // If category not found, return empty
                    bots = Page.empty(pageable);
                }
            }
            // CASE 3: has tag, no category
            else if (!hasCategory && hasTag) {
                Pageable unsortedPageable = PageRequest.of(page, size);
                bots = botRepository.findByTags(new String[]{tag}, unsortedPageable);
            }
            // CASE 4: has both category and tag
            else {
                Optional<Category> cate = Optional.empty();

                if (category.matches("\\d+")) {
                    cate = categoryRepository.findById(Long.parseLong(category));
                }

                if (cate.isPresent()) {
                    List<Tags> tags = tagsRepository.findAllByCategoryId(cate.get().getId());
                    List<String> tagNames = tags.stream()
                            .map(Tags::getName)
                            .collect(Collectors.toList());

                    // Only keep the passed tag if it is in that category
                    if (tagNames.contains(tag)) {
                        Pageable unsortedPageable = PageRequest.of(page, size);
                        bots = botRepository.findByTags(new String[]{tag}, unsortedPageable);
                    } else {
                        bots = Page.empty(pageable);
                    }
                } else {
                    bots = Page.empty(pageable);
                }
            }
        }

        return bots.map(bot -> mapToBotListResponse(bot, lang));
    }


    public BotResponse getBotById(Long id) {
        Bot bot = botRepository.findByIdWithDetails(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bot not found with id: " + id));
        
        return mapToBotResponse(bot);
    }
    
    public BotResponse getBotBySlug(String slug) {
        Bot bot = botRepository.findBySlugWithDetails(slug)
            .orElseThrow(() -> new ResourceNotFoundException("Bot not found with slug: " + slug));
        
        return mapToBotResponse(bot);
    }
    
    public BotResponse createBot(BotRequest request) {
        if (botRepository.existsBySlug(request.getSlug())) {
            throw new BadRequestException("Bot with slug '" + request.getSlug() + "' already exists");
        }
        
        if (botRepository.existsByExternalKey(request.getExternalKey())) {
            throw new BadRequestException("Bot with external key '" + request.getExternalKey() + "' already exists");
        }
        
        Bot bot = new Bot();
        updateBotFromRequest(bot, request);
        
        Bot savedBot = botRepository.save(bot);
        log.info("Created new bot with slug: {}", savedBot.getSlug());
        
        return mapToBotResponse(savedBot);
    }
    
    public BotResponse updateBot(Long id, BotRequest request) {
        Bot bot = botRepository.findByIdWithDetails(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bot not found with id: " + id));
        
        // Check if slug is being changed and if new slug already exists
        if (!bot.getSlug().equals(request.getSlug()) && 
            botRepository.existsBySlug(request.getSlug())) {
            throw new BadRequestException("Bot with slug '" + request.getSlug() + "' already exists");
        }
        
        // Check if external key is being changed and if new key already exists
        if (!bot.getExternalKey().equals(request.getExternalKey()) && 
            botRepository.existsByExternalKey(request.getExternalKey())) {
            throw new BadRequestException("Bot with external key '" + request.getExternalKey() + "' already exists");
        }
        
        updateBotFromRequest(bot, request);
        
        Bot updatedBot = botRepository.save(bot);
        log.info("Updated bot with id: {}", updatedBot.getId());
        
        return mapToBotResponse(updatedBot);
    }
    
    public void deleteBot(Long id) {
        if (!botRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bot not found with id: " + id);
        }
        
        botRepository.deleteById(id);
        log.info("Deleted bot with id: {}", id);
    }

    public List<CategoryResponse> getAllCategories() {
        List<CategoryResponse.Tags> tags = tagsRepository.findAll().stream()
                .map(tag -> CategoryResponse.Tags.builder()
                        .id(tag.getId())
                        .name(tag.getName())
                        .categoryId(tag.getCategory().getId())
                        .createdAt(tag.getCreatedAt())
                        .updatedAt(tag.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());

        // Map category -> CategoryResponse
        return categoryRepository.findAll().stream()
                .map(category -> CategoryResponse.builder()
                        .id(category.getId())
                        .title(category.getTitle())
                        .desc(category.getDesc())
                        .createdAt(category.getCreatedAt())
                        .updatedAt(category.getUpdatedAt())
                        .tags(
                                tags.stream()
                                        .filter(t -> t.getCategoryId().equals(category.getId()))
                                        .collect(Collectors.toList())
                        )
                        .build())
                .collect(Collectors.toList());
    }

    private void updateBotFromRequest(Bot bot, BotRequest request) {
        bot.setExternalId(request.getExternalId());
        bot.setExternalKey(request.getExternalKey());
        bot.setSlug(request.getSlug());
        bot.setImage(request.getImage());
        bot.setAffiliateLink(request.getAffiliateLink());
        bot.setNameVi(request.getNameVi());
        bot.setNameEn(request.getNameEn());
        bot.setSummaryVi(request.getSummaryVi());
        bot.setSummaryEn(request.getSummaryEn());
        
        if (request.getTags() != null) {
            bot.setTags(request.getTags().toArray(new String[0]));
        }
        
        // Update features
        bot.getFeatures().clear();
        if (request.getFeatures() != null) {
            for (BotRequest.FeatureRequest featureReq : request.getFeatures()) {
                BotFeature feature = new BotFeature();
                feature.setContentVi(featureReq.getContentVi());
                feature.setContentEn(featureReq.getContentEn());
                feature.setDisplayOrder(featureReq.getDisplayOrder());
                bot.addFeature(feature);
            }
        }
        
        // Update strengths
        bot.getStrengths().clear();
        if (request.getStrengths() != null) {
            for (BotRequest.StrengthRequest strengthReq : request.getStrengths()) {
                BotStrength strength = new BotStrength();
                strength.setContentVi(strengthReq.getContentVi());
                strength.setContentEn(strengthReq.getContentEn());
                strength.setDisplayOrder(strengthReq.getDisplayOrder());
                bot.addStrength(strength);
            }
        }
        
        // Update weaknesses
        bot.getWeaknesses().clear();
        if (request.getWeaknesses() != null) {
            for (BotRequest.WeaknessRequest weaknessReq : request.getWeaknesses()) {
                BotWeakness weakness = new BotWeakness();
                weakness.setContentVi(weaknessReq.getContentVi());
                weakness.setContentEn(weaknessReq.getContentEn());
                weakness.setDisplayOrder(weaknessReq.getDisplayOrder());
                bot.addWeakness(weakness);
            }
        }
        
        // Update target users
        bot.getTargetUsers().clear();
        if (request.getTargetUsers() != null) {
            for (BotRequest.TargetUserRequest targetUserReq : request.getTargetUsers()) {
                BotTargetUser targetUser = new BotTargetUser();
                targetUser.setContentVi(targetUserReq.getContentVi());
                targetUser.setContentEn(targetUserReq.getContentEn());
                targetUser.setDisplayOrder(targetUserReq.getDisplayOrder());
                bot.addTargetUser(targetUser);
            }
        }
        
        // Update pricing plans
        bot.getPricingPlans().clear();
        if (request.getPricingPlans() != null) {
            for (BotRequest.PricingRequest pricingReq : request.getPricingPlans()) {
                BotPricing pricing = new BotPricing();
                pricing.setPlanVi(pricingReq.getPlanVi());
                pricing.setPlanEn(pricingReq.getPlanEn());
                pricing.setPriceTextVi(pricingReq.getPriceTextVi());
                pricing.setPriceTextEn(pricingReq.getPriceTextEn());
                pricing.setAmount(pricingReq.getAmount());
                pricing.setCurrency(pricingReq.getCurrency());
                pricing.setInterval(pricingReq.getInterval());
                pricing.setDisplayOrder(pricingReq.getDisplayOrder());
                bot.addPricing(pricing);
            }
        }
    }
    
    private BotListResponse mapToBotListResponse(Bot bot, String lang) {
        return BotListResponse.builder()
            .id(bot.getId())
            .externalKey(bot.getExternalKey())
            .slug(bot.getSlug())
            .name("vi".equals(lang) ? bot.getNameVi() : bot.getNameEn())
            .summary("vi".equals(lang) ? bot.getSummaryVi() : bot.getSummaryEn())
            .image(bot.getImage())
            .affiliateLink(bot.getAffiliateLink())
            .tags(bot.getTags() != null ? Arrays.asList(bot.getTags()) : List.of())
            .createdAt(bot.getCreatedAt())
            .updatedAt(bot.getUpdatedAt())
            .build();
    }
    
    private BotResponse mapToBotResponse(Bot bot) {
        return BotResponse.builder()
            .id(bot.getId())
            .externalId(bot.getExternalId())
            .externalKey(bot.getExternalKey())
            .slug(bot.getSlug())
            .image(bot.getImage())
            .affiliateLink(bot.getAffiliateLink())
            .name(BotResponse.MultiLangText.builder()
                .vi(bot.getNameVi())
                .en(bot.getNameEn())
                .build())
            .summary(BotResponse.MultiLangText.builder()
                .vi(bot.getSummaryVi())
                .en(bot.getSummaryEn())
                .build())
            .tags(bot.getTags() != null ? Arrays.asList(bot.getTags()) : List.of())
            .features(mapFeatures(bot.getFeatures()))
            .strengths(mapStrengths(bot.getStrengths()))
            .weaknesses(mapWeaknesses(bot.getWeaknesses()))
            .targetUsers(mapTargetUsers(bot.getTargetUsers()))
            .pricingPlans(mapPricingPlans(bot.getPricingPlans()))
            .createdAt(bot.getCreatedAt())
            .updatedAt(bot.getUpdatedAt())
            .build();
    }
    
    private List<BotResponse.MultiLangItem> mapFeatures(List<BotFeature> features) {
        return features.stream()
            .map(f -> BotResponse.MultiLangItem.builder()
                .id(f.getId())
                .contentVi(f.getContentVi())
                .contentEn(f.getContentEn())
                .displayOrder(f.getDisplayOrder())
                .build())
            .collect(Collectors.toList());
    }
    
    private List<BotResponse.MultiLangItem> mapStrengths(List<BotStrength> strengths) {
        return strengths.stream()
            .map(s -> BotResponse.MultiLangItem.builder()
                .id(s.getId())
                .contentVi(s.getContentVi())
                .contentEn(s.getContentEn())
                .displayOrder(s.getDisplayOrder())
                .build())
            .collect(Collectors.toList());
    }
    
    private List<BotResponse.MultiLangItem> mapWeaknesses(List<BotWeakness> weaknesses) {
        return weaknesses.stream()
            .map(w -> BotResponse.MultiLangItem.builder()
                .id(w.getId())
                .contentVi(w.getContentVi())
                .contentEn(w.getContentEn())
                .displayOrder(w.getDisplayOrder())
                .build())
            .collect(Collectors.toList());
    }
    
    private List<BotResponse.MultiLangItem> mapTargetUsers(List<BotTargetUser> targetUsers) {
        return targetUsers.stream()
            .map(t -> BotResponse.MultiLangItem.builder()
                .id(t.getId())
                .contentVi(t.getContentVi())
                .contentEn(t.getContentEn())
                .displayOrder(t.getDisplayOrder())
                .build())
            .collect(Collectors.toList());
    }
    
    private List<BotResponse.PricingPlan> mapPricingPlans(List<BotPricing> pricingPlans) {
        return pricingPlans.stream()
            .map(p -> BotResponse.PricingPlan.builder()
                .id(p.getId())
                .plan(BotResponse.MultiLangText.builder()
                    .vi(p.getPlanVi())
                    .en(p.getPlanEn())
                    .build())
                .priceText(BotResponse.MultiLangText.builder()
                    .vi(p.getPriceTextVi())
                    .en(p.getPriceTextEn())
                    .build())
                .amount(p.getAmount())
                .currency(p.getCurrency())
                .interval(p.getInterval())
                .displayOrder(p.getDisplayOrder())
                .build())
            .collect(Collectors.toList());
    }
}