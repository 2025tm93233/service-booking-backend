package com.servicebooking.config;

import com.servicebooking.model.*;
import com.servicebooking.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("Initializing database data...");

        // Initialize categories
        initializeCategories();

        // Initialize users
        initializeUsers();

        // Initialize services
        initializeServices();

        log.info("Database initialization completed!");
    }

    private void initializeCategories() {
        if (categoryRepository.count() == 0) {
            List<Category> categories = Arrays.asList(
                    Category.builder()
                            .name("Plumbing")
                            .description("Professional plumbing services for repairs and installations")
                            .icon("🔧")
                            .slug("plumbing")
                            .build(),
                    Category.builder()
                            .name("Electrical")
                            .description("Expert electrical work for homes and businesses")
                            .icon("⚡")
                            .slug("electrical")
                            .build(),
                    Category.builder()
                            .name("Tutoring")
                            .description("Personal tutoring for all subjects and levels")
                            .icon("📚")
                            .slug("tutoring")
                            .build(),
                    Category.builder()
                            .name("Cleaning")
                            .description("Professional cleaning services for your space")
                            .icon("🧹")
                            .slug("cleaning")
                            .build(),
                    Category.builder()
                            .name("Gardening")
                            .description("Expert gardening and landscaping services")
                            .icon("🌱")
                            .slug("gardening")
                            .build(),
                    Category.builder()
                            .name("Moving")
                            .description("Reliable moving and packing services")
                            .icon("📦")
                            .slug("moving")
                            .build()
            );

            categoryRepository.saveAll(categories);
            log.info("Initialized {} categories", categories.size());
        } else {
            log.info("Categories already exist, skipping initialization");
        }
    }

    private void initializeUsers() {
        if (userRepository.count() == 0) {
            List<User> users = Arrays.asList(
                    User.builder()
                            .name("John Doe")
                            .email("customer@example.com")
                            .password(passwordEncoder.encode("password123"))
                            .role(UserRole.CUSTOMER)
                            .phone("+1234567890")
                            .avatar("https://api.dicebear.com/7.x/avataaars/svg?seed=John")
                            .build(),
                    User.builder()
                            .name("Jane Smith")
                            .email("provider1@example.com")
                            .password(passwordEncoder.encode("password123"))
                            .role(UserRole.PROVIDER)
                            .phone("+1234567891")
                            .avatar("https://api.dicebear.com/7.x/avataaars/svg?seed=Jane")
                            .build(),
                    User.builder()
                            .name("Bob Johnson")
                            .email("provider2@example.com")
                            .password(passwordEncoder.encode("password123"))
                            .role(UserRole.PROVIDER)
                            .phone("+1234567892")
                            .avatar("https://api.dicebear.com/7.x/avataaars/svg?seed=Bob")
                            .build()
            );

            userRepository.saveAll(users);
            log.info("Initialized {} users", users.size());
        } else {
            log.info("Users already exist, skipping initialization");
        }
    }

    private void initializeServices() {
        if (serviceRepository.count() == 0) {
            List<Category> categories = categoryRepository.findAll();
            List<User> providers = userRepository.findByRole(UserRole.PROVIDER);

            if (categories.isEmpty() || providers.isEmpty()) {
                log.warn("Cannot initialize services - missing categories or providers");
                return;
            }

            Category plumbing = categories.get(0);
            Category electrical = categories.get(1);
            Category tutoring = categories.get(2);
            Category cleaning = categories.get(3);
            Category gardening = categories.get(4);
            Category moving = categories.get(5);

            User provider1 = providers.get(0);
            User provider2 = providers.get(1);

            List<Service> services = Arrays.asList(
                    Service.builder()
                            .title("Emergency Plumbing Repair")
                            .description("Fast and reliable emergency plumbing services. Available 24/7 for all your plumbing needs including leaks, clogs, and pipe repairs.")
                            .category(plumbing)
                            .providerId(provider1.getId())
                            .provider(provider1)
                            .price(75.0)
                            .priceUnit(PriceUnit.HOUR)
                            .images(Arrays.asList("https://images.unsplash.com/photo-1504328345606-18bbc8c9d7d1?w=400"))
                            .rating(4.8)
                            .reviewCount(124)
                            .location("New York, NY")
                            .availability(Arrays.asList("monday", "tuesday", "wednesday", "thursday", "friday"))
                            .tags(Arrays.asList("emergency", "repair", "24/7"))
                            .isActive(true)
                            .build(),
                    Service.builder()
                            .title("Electrical Panel Installation")
                            .description("Professional electrical panel installation and upgrades. Licensed electrician with 10+ years of experience.")
                            .category(electrical)
                            .providerId(provider2.getId())
                            .provider(provider2)
                            .price(150.0)
                            .priceUnit(PriceUnit.FIXED)
                            .images(Arrays.asList("https://images.unsplash.com/photo-1621905251189-08b45d6a269e?w=400"))
                            .rating(4.9)
                            .reviewCount(89)
                            .location("Los Angeles, CA")
                            .availability(Arrays.asList("monday", "wednesday", "friday"))
                            .tags(Arrays.asList("installation", "upgrade", "licensed"))
                            .isActive(true)
                            .build(),
                    Service.builder()
                            .title("Math Tutoring - All Levels")
                            .description("Experienced math tutor for all levels from elementary to college. Personalized learning approach.")
                            .category(tutoring)
                            .providerId(provider1.getId())
                            .provider(provider1)
                            .price(50.0)
                            .priceUnit(PriceUnit.HOUR)
                            .images(Arrays.asList("https://images.unsplash.com/photo-1522202176988-66273c2fd55f?w=400"))
                            .rating(4.7)
                            .reviewCount(56)
                            .location("Chicago, IL")
                            .availability(Arrays.asList("tuesday", "thursday", "saturday"))
                            .tags(Arrays.asList("math", "tutoring", "all-levels"))
                            .isActive(true)
                            .build(),
                    Service.builder()
                            .title("Deep House Cleaning")
                            .description("Comprehensive deep cleaning service for your entire home. Includes all rooms, kitchen, and bathrooms.")
                            .category(cleaning)
                            .providerId(provider2.getId())
                            .provider(provider2)
                            .price(200.0)
                            .priceUnit(PriceUnit.FIXED)
                            .images(Arrays.asList("https://images.unsplash.com/photo-1581578731548-c64695cc6952?w=400"))
                            .rating(4.6)
                            .reviewCount(203)
                            .location("Houston, TX")
                            .availability(Arrays.asList("monday", "tuesday", "wednesday", "thursday", "friday"))
                            .tags(Arrays.asList("cleaning", "deep-clean", "home"))
                            .isActive(true)
                            .build(),
                    Service.builder()
                            .title("Garden Design & Maintenance")
                            .description("Professional garden design and ongoing maintenance services. Transform your outdoor space.")
                            .category(gardening)
                            .providerId(provider1.getId())
                            .provider(provider1)
                            .price(65.0)
                            .priceUnit(PriceUnit.HOUR)
                            .images(Arrays.asList("https://images.unsplash.com/photo-1416879595882-3373a0480b5b?w=400"))
                            .rating(4.5)
                            .reviewCount(78)
                            .location("Phoenix, AZ")
                            .availability(Arrays.asList("saturday", "sunday"))
                            .tags(Arrays.asList("gardening", "design", "maintenance"))
                            .isActive(true)
                            .build(),
                    Service.builder()
                            .title("Full Service Moving")
                            .description("Complete moving service including packing, loading, transportation, and unpacking. Stress-free relocation.")
                            .category(moving)
                            .providerId(provider2.getId())
                            .provider(provider2)
                            .price(500.0)
                            .priceUnit(PriceUnit.FIXED)
                            .images(Arrays.asList("https://images.unsplash.com/photo-1600518464441-9154a4dea21b?w=400"))
                            .rating(4.8)
                            .reviewCount(145)
                            .location("Miami, FL")
                            .availability(Arrays.asList("monday", "tuesday", "wednesday", "thursday", "friday", "saturday"))
                            .tags(Arrays.asList("moving", "packing", "relocation"))
                            .isActive(true)
                            .build()
            );

            serviceRepository.saveAll(services);
            log.info("Initialized {} services", services.size());
        } else {
            log.info("Services already exist, skipping initialization");
        }
    }
}
