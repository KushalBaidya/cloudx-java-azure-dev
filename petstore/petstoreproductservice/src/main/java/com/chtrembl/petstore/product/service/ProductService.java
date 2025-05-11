package com.chtrembl.petstore.product.service;

import com.chtrembl.petstore.product.model.Product;
import com.chtrembl.petstore.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Method to get a Product by ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Method to add a new Product
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    // Method to update an existing Product
    public Product updateProduct(Product product) {
        if (product.getId() != null && productRepository.existsById(product.getId())) {
            return productRepository.save(product);
        }
        throw new IllegalArgumentException("Product with ID " + product.getId() + " does not exist.");
    }

    // Method to delete a Product by ID
    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Product with ID " + id + " does not exist.");
        }
    }

    // Method to find Products by status
    public List<Product> findProductsByStatus(Product.StatusEnum status) {
        return productRepository.findByStatus(status);
    }
}
