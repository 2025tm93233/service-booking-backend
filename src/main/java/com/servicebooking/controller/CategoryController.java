package com.servicebooking.controller;

import com.servicebooking.dto.ApiResponse;
import com.servicebooking.model.Category;
import com.servicebooking.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Categories", description = "APIs for managing service categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Get all categories", description = "Retrieve all service categories")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<Category>>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    @Operation(summary = "Get category by ID", description = "Retrieve a specific category by its ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> getCategoryById(@Parameter(description = "Category ID") @PathVariable String id) {
        Optional<Category> category = categoryService.getCategoryById(id);
        if (category.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(category.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get category by slug", description = "Retrieve a category by its slug")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<Category>> getCategoryBySlug(@Parameter(description = "Category slug") @PathVariable String slug) {
        Optional<Category> category = categoryService.getCategoryBySlug(slug);
        if (category.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(category.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create category", description = "Create a new service category")
    @SecurityRequirement(name = "bearer-jwt")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category created successfully", content = @Content(schema = @Schema(implementation = Category.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input or slug already exists"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<Category>> createCategory(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Category details", required = true, content = @Content(schema = @Schema(implementation = Category.class))) @RequestBody Category category) {
        try {
            if (categoryService.existsBySlug(category.getSlug())) {
                return ResponseEntity.badRequest().body(ApiResponse.<Category>error("Slug already exists"));
            }
            Category createdCategory = categoryService.createCategory(category);
            return ResponseEntity.ok(ApiResponse.success(createdCategory, "Category created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Category>error(e.getMessage()));
        }
    }

    @Operation(summary = "Update category", description = "Update an existing category")
    @SecurityRequirement(name = "bearer-jwt")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category updated successfully", content = @Content(schema = @Schema(implementation = Category.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> updateCategory(
            @Parameter(description = "Category ID") @PathVariable String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated category details", required = true, content = @Content(schema = @Schema(implementation = Category.class))) @RequestBody Category category) {
        try {
            Category updatedCategory = categoryService.updateCategory(id, category);
            return ResponseEntity.ok(ApiResponse.success(updatedCategory, "Category updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Category>error(e.getMessage()));
        }
    }

    @Operation(summary = "Delete category", description = "Delete a category")
    @SecurityRequirement(name = "bearer-jwt")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@Parameter(description = "Category ID") @PathVariable String id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(ApiResponse.success(null, "Category deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Void>error(e.getMessage()));
        }
    }
}