package com.example.my_ecomerce_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Controller
public class ProductController {

    private final ProductRepository productRepository;
    private final Path root = Paths.get("src/main/resources/static/uploads");
    @Autowired
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //Display Form to upload Products
    @GetMapping("/addproduct")
    public String getAddProduct() {
        return "admin/addproduct";
    }


    //Upload Product with image and submit to database
    @PostMapping("/putproduct")
    public String addProduct(@RequestParam("image") MultipartFile image, @RequestParam("name") String name, @RequestParam("description") String description, @RequestParam("rating") Integer rating) {

        String imageUrl = StringUtils.cleanPath(image.getOriginalFilename()) ;
 String imagePath = "uploads/"+imageUrl;

        try {
            Files.copy(image.getInputStream(), this.root.resolve(image.getOriginalFilename()));

            //Save to Database
            Product product = Product.builder()
                    .image(imagePath)
                    .name(name)
                    .description(description)
                    .rating(rating)
                    .build();

            productRepository.save(product);

        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException ) {
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }

        return "shop";

    }


    //View All Products From Admin Panel
    @GetMapping("/products")
        public String getProducts(Model model){
       model.addAttribute("product", productRepository.findAll());

        return "admin/productview";
        }





}