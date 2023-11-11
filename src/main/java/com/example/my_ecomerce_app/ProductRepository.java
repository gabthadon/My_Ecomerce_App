package com.example.my_ecomerce_app;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE product SET description= ?1, name=?2, rating=?3, image=?4 WHERE id=?5",

    nativeQuery = true)

  int  updateProductById(String description, String name, Integer rating, String image, Long id);



}
