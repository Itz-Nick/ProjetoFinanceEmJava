package com.nikofinance.config;

import com.nikofinance.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoryService categoryService;

    @Override
    public void run(String... args) {
        categoryService.initializeDefaultCategories();
    }
}