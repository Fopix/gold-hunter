package com.gold_hunter.gold_hunter.controllers;

import com.gold_hunter.gold_hunter.models.Review;
import com.gold_hunter.gold_hunter.repositorys.ReviewsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    private final ReviewsRepository reviewsRepository;

    public MainController(ReviewsRepository reviewsRepository) {
        this.reviewsRepository = reviewsRepository;
    }

    @GetMapping("/")
    public String index() { return "index"; }

    @GetMapping("/guarantee")
    public String guarantee() {
        return "guarantee";
    }

    @GetMapping("/contractor")
    public String contractor() {
        return "contractor";
    }

    @GetMapping("/faq")
    public String faq() {
        return "faq";
    }

    @GetMapping("/contacts")
    public String contacts() {
        return "contacts";
    }

    @GetMapping("/contract")
    public String contract() {
        return "contract";
    }

    @GetMapping("/privacy")
    public String privacy() {
        return "privacy";
    }

    @GetMapping("/reviews")
    public String reviews(Model model, @RequestParam(value = "page", required = false, defaultValue = "0") int page) {
        Page<Review> pageReview = reviewsRepository.findAll(PageRequest.of(page, 10, Sort.by("id").descending()));

        model.addAttribute("page", pageReview);

        return "reviews";
    }
}