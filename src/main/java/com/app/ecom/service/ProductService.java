package com.app.ecom.service;

import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;
import com.app.ecom.model.Product;
import com.app.ecom.repositories.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;


    public void productRequestToProduct(Product product, ProductRequest productRequest) {
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setCategory(productRequest.getCategory());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setImageUrl(productRequest.getImageUrl());
    }

    public ProductResponse productToProductResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setName(product.getName());
        productResponse.setCategory(product.getCategory());
        productResponse.setDescription(product.getDescription());
        productResponse.setPrice(product.getPrice());
        productResponse.setStockQuantity(product.getStockQuantity());
        productResponse.setActive(product.getActive());
        productResponse.setId(product.getId());
        productResponse.setImageUrl(product.getImageUrl());

        return productResponse;
    }


    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();
        productRequestToProduct(product, productRequest);
        Product savedProduct = productRepo.save(product);
        return productToProductResponse(savedProduct);

    }

    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        return productRepo.findById(id)
                .map(existingProduct -> {
                    productRequestToProduct(existingProduct, productRequest);
                    Product savedProduct = productRepo.save(existingProduct);
                    return productToProductResponse(savedProduct);

                });
    }

    public List<ProductResponse> fetchAllProduct() {
        return productRepo.findByActiveTrue().stream()
                .map(this::productToProductResponse)
                .collect(Collectors.toList());

    }

    public boolean deleteProduct(Long id) {

        return productRepo.findById(id).map(product -> {
            product.setActive(false);
            productRepo.save(product);
            return true;
        }).orElse(false);
    }

    public List<ProductResponse> findProducts(String keyword) {
        return productRepo.searchProducts(keyword).stream().map(this::productToProductResponse).collect(Collectors.toList());
    }
}
