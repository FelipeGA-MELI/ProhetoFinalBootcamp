package com.mercadolibre.dambetan01.service.impl;

import com.mercadolibre.dambetan01.dtos.ProductListDTO;
import com.mercadolibre.dambetan01.dtos.response.BatchStockDTO;
import com.mercadolibre.dambetan01.dtos.ProductDTO;
import com.mercadolibre.dambetan01.dtos.response.ProductStockSearchDTO;
import com.mercadolibre.dambetan01.dtos.response.ProductWithIdResponse;
import com.mercadolibre.dambetan01.exceptions.NotFoundException;
import com.mercadolibre.dambetan01.exceptions.ProductAlreadyRegistered;
import com.mercadolibre.dambetan01.model.Category;
import com.mercadolibre.dambetan01.model.Product;
import com.mercadolibre.dambetan01.model.user.Seller;
import com.mercadolibre.dambetan01.repository.CategoryRepository;
import com.mercadolibre.dambetan01.repository.ProductRepository;
import com.mercadolibre.dambetan01.repository.ProductStockRepository;
import com.mercadolibre.dambetan01.repository.SellerRepository;
import com.mercadolibre.dambetan01.service.ProductService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    final private ProductRepository repository;
    final private ProductStockRepository productStockRepository;
    final private SellerRepository sellerRepository;
    final private CategoryRepository categoryRepository;
    final private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository repository, ProductStockRepository productStockRepository,
                              SellerRepository sellerRepository, CategoryRepository categoryRepository,
                              ProductRepository productRepository) {
        this.repository = repository;
        this.productStockRepository = productStockRepository;
        this.sellerRepository = sellerRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Product findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found product with id "+id));
    }

    @Override
    public List<ProductListDTO> findAllProductsList() {

        LocalDate today = LocalDate.now();
        LocalDate threeWeeksAgo = today.minusWeeks(3);

        List<ProductListDTO> productList = repository.findAllProductsList(threeWeeksAgo);

        if (productList.size() == 0) throw new NotFoundException("Not found products");

       return productList;
    }

    @Override
    public List<ProductListDTO> findAllProductsListByCategory(String category) {

        String nameCategory = "";

        if(category.equalsIgnoreCase("FR")){
            nameCategory = "fresco";
        } else if (category.equalsIgnoreCase("RF")){
             nameCategory = "refrigerado";
        } else {
             nameCategory = "congelado";
        }

        LocalDate today = LocalDate.now();
        LocalDate threeWeeksAgo = today.minusWeeks(3);

        String finalNameCategory = nameCategory;
        List<ProductListDTO> productList = repository.findAllProductsList(threeWeeksAgo).stream()
                .filter(data -> data.getCategory().equals(finalNameCategory)).collect(Collectors.toList());

        if (productList.size() == 0) throw new NotFoundException("Not found products");

        return productList;
    }

    @Override
    public ProductStockSearchDTO findAllProductsByIdAndSort(Long idProduct, String order) {
        ProductStockSearchDTO productStockSearchDTO = new ProductStockSearchDTO();
        productStockSearchDTO.setProductId(idProduct);
        List<BatchStockDTO> byProductId = productStockRepository.findByProductId(idProduct, getProductStockSortByOrder(order));
        productStockSearchDTO.setSector(productStockRepository.findByProductBySector(idProduct));
        productStockSearchDTO.setBatchStock(byProductId);
        return productStockSearchDTO;
    }

    @Override
    public ProductWithIdResponse resgisterProduct(ProductDTO productDTO) {
        Optional<Seller> seller = checkIfSellerExists(productDTO.getSellerId());
        Optional<Category> category = checkIfCategoryExists(productDTO.getCategoryId());
        Optional<Product> product = productRepository.findByName(productDTO.getName());

        if(product.isPresent())
            throw new ProductAlreadyRegistered("Product has been already created");

        Product productModel = new Product();
        productModel.setName(productDTO.getName());
        productModel.setSeller(seller.get());
        productModel.setCategory(category.get());
        productModel.setPrice(productDTO.getPrice());

        Product savedProduct = productRepository.save(productModel);
        ProductWithIdResponse finalProduct = new ProductWithIdResponse();
        finalProduct.setId(savedProduct.getId());
        finalProduct.setName(savedProduct.getName());
        finalProduct.setSellerId(savedProduct.getSeller().getId());
        finalProduct.setCategoryId(savedProduct.getCategory().getId());
        finalProduct.setPrice(savedProduct.getPrice());

        return finalProduct;
    }

    @Override
    public ProductWithIdResponse updateProduct(ProductDTO productDTO, Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        Optional<Seller> seller = checkIfSellerExists(productDTO.getSellerId());
        Optional<Category> category = checkIfCategoryExists(productDTO.getCategoryId());

        if(product.isEmpty())
            throw new NotFoundException("Product does not exist");

        Product productModel = new Product();
        productModel.setId(productId);
        productModel.setName(productDTO.getName());
        productModel.setSeller(seller.get());
        productModel.setCategory(category.get());
        productModel.setPrice(productDTO.getPrice());

        Product savedProduct = productRepository.save(productModel);
        ProductWithIdResponse finalProduct = new ProductWithIdResponse();
        finalProduct.setId(savedProduct.getId());
        finalProduct.setName(savedProduct.getName());
        finalProduct.setSellerId(savedProduct.getSeller().getId());
        finalProduct.setCategoryId(savedProduct.getCategory().getId());
        finalProduct.setPrice(savedProduct.getPrice());

        return finalProduct;
    }

    private Sort getProductStockSortByOrder(String order) {
        if (order == null)
            return null;
        switch (order){
            case "L":
                return Sort.by(Sort.Direction.DESC, "id");
            case "C":
                return Sort.by(Sort.Direction.DESC, "currentQuantity");
            case "F":
                return Sort.by(Sort.Direction.DESC, "dueDate");
            default:
                return null;
        }
    }

    private Optional<Seller> checkIfSellerExists(Long sellerId) {
        Optional<Seller> seller = sellerRepository.findById(sellerId);

        if(seller.isEmpty())
            throw new NotFoundException("Seller not found.");

        return seller;
    }

    private Optional<Category> checkIfCategoryExists(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);

        if(category.isEmpty())
            throw new NotFoundException("Category not found.");

        return category;
    }
}
