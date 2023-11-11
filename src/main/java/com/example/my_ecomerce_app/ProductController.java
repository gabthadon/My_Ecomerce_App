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
import java.util.Optional;


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

        String imageUrl = StringUtils.cleanPath(image.getOriginalFilename());


        try {
            Files.copy(image.getInputStream(), this.root.resolve(image.getOriginalFilename()));

            //Save to Database
            Product product = Product.builder()
                    .image(imageUrl)
                    .name(name)
                    .description(description)
                    .rating(rating)
                    .build();

            productRepository.save(product);

        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }

        return "redirect:products";

    }


    //View All Products From Admin Panel
    @GetMapping("/products")
    public String getProducts(Model model) {
        model.addAttribute("product", productRepository.findAll());

        return "admin/productview";
    }

    @GetMapping("delete/{id}")
    public String deleteProduct(@PathVariable Long id) {

        productRepository.deleteById(id);
        return "redirect:/products";
    }

    @GetMapping("updateproduct/{description}/{name}/{rating}/{image}/{id}")
    public String getUpdateProduct(Model model, @PathVariable String description, @PathVariable String name, @PathVariable Integer rating, @PathVariable(required = false) String image, @PathVariable Long id) {
        model.addAttribute("name", name);
        model.addAttribute("description", description);
        model.addAttribute("rating", rating);
        model.addAttribute("image", image);
        model.addAttribute("id", id);
        return "/admin/productupdate";
    }

    @PostMapping("/update/product")
    public String updateProduct(@RequestParam String description, @RequestParam String name, @RequestParam Integer rating, @RequestParam MultipartFile image, @RequestParam Long id) {

        String imageUrl = StringUtils.cleanPath(image.getOriginalFilename());


        try {
            Files.copy(image.getInputStream(), this.root.resolve(image.getOriginalFilename()));

            int myProductUpdate = productRepository.updateProductById(description, name, rating, imageUrl, id);

            if (myProductUpdate == 1)
                return "redirect:/products";


        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }


        }

        return "redirect:/products";

    }
}