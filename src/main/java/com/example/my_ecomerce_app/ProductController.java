package com.example.my_ecomerce_app;

import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.nio.file.Paths;


@Controller
public class ProductController {

    private final ProductRepository productRepository;
    private final Path root = Paths.get("uploads");
    @Autowired
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/addproduct")
    public String getAddProduct() {
        return "admin/addproduct";
    }


    @PostMapping("/putproduct")
    public String addProduct(@RequestParam("image") MultipartFile image, @RequestParam("name") String name, @RequestParam("description") String description, @RequestParam("rating") Integer rating) {

       // String imageUrl = StringUtils.cleanPath(image.getOriginalFilename()) ;


        try {
            Files.copy(image.getInputStream(), this.root.resolve(image.getOriginalFilename()));
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException ) {
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }


/*
        Product product = Product.builder()
                .image(imageUrl)
                .name(name)
                .description(description)
                .rating(rating)
                .build();

        productRepository.save(product);


 */

        return "shop";


    }





}