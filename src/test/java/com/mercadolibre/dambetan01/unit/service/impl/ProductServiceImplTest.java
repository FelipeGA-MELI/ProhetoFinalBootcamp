package com.mercadolibre.dambetan01.unit.service.impl;

import com.mercadolibre.dambetan01.dtos.ProductDTO;
import com.mercadolibre.dambetan01.dtos.ProductListDTO;
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
import com.mercadolibre.dambetan01.service.impl.ProductServiceImpl;
import org.aspectj.weaver.ast.Not;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {
    private ProductRepository repository;
    private SellerRepository sellerRepository;
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;
    private ProductServiceImpl service;
    private Product product;
    private Seller seller;
    private Category category;
    private static List<ProductListDTO> productListDTO;


    @BeforeEach
    public void setUp(){
        seller = mock(Seller.class);
        category = mock(Category.class);
        repository = mock(ProductRepository.class);
        sellerRepository = mock(SellerRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        productRepository = mock(ProductRepository.class);
        product = mock(Product.class);
        final ProductStockRepository productStockRepository = mock(ProductStockRepository.class);
        service = new ProductServiceImpl(repository, productStockRepository, sellerRepository, categoryRepository,
               productRepository);
        productListDTO = List
                .of(new ProductListDTO(1L,"Uva", "fresco", LocalDate.of(2021,12,1)));
    }

    @Test
    public void shouldFindByIdCorrectly() {
        Long id = 1l;
        when(repository.findById(id)).thenReturn(Optional.of(new Product()));
        service.findById(id);
        verify(repository, times(1)).findById(id);
    }

    @Test
    public void shouldShowExceptionWhenFindById() {
        Long id = 1l;
        when(repository.findById(id)).thenReturn(Optional.empty());


        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.findById(id);
        });

        String expectedMessage = "Not Found Exception. Not found product with id 1";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(repository, times(1)).findById(id);
    }

    @Test
    public void shouldFindAllProductsListCorrectly(){

        LocalDate today = LocalDate.now();
        LocalDate threeWeeksAgo = today.minusWeeks(3);

        when(repository.findAllProductsList(threeWeeksAgo)).thenReturn(productListDTO);
        service.findAllProductsList();
        verify(repository, times(1)).findAllProductsList(threeWeeksAgo);

    }

    @Test
    public void shouldShowExceptionWhenFindAllProductList(){

        LocalDate today = LocalDate.now();
        LocalDate threeWeeksAgo = today.minusWeeks(3);

        when(repository.findAllProductsList(threeWeeksAgo)).thenReturn(List.of());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.findAllProductsList();
        });

        String expectedMessage = "Not Found Exception. Not found products";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(repository, times(1)).findAllProductsList(threeWeeksAgo);
    }

    @Test
    public void shouldFindAllProductsListByCategoryCorrectly(){

        LocalDate today = LocalDate.now();
        LocalDate threeWeeksAgo = today.minusWeeks(3);

        when(repository.findAllProductsList(threeWeeksAgo)).thenReturn(productListDTO);
        service.findAllProductsListByCategory("FR");
        verify(repository, times(1)).findAllProductsList(threeWeeksAgo);

    }

    @Test
    public void shouldShowExceptionWhenFindAllProductListByCategory(){

        LocalDate today = LocalDate.now();
        LocalDate threeWeeksAgo = today.minusWeeks(3);

        when(repository.findAllProductsList(threeWeeksAgo)).thenReturn(productListDTO);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.findAllProductsListByCategory("RF");
        });

        String expectedMessage = "Not Found Exception. Not found products";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(repository, times(1)).findAllProductsList(threeWeeksAgo);
    }

    @Test
    public void shouldReturnCreatedProduct() {
        ProductDTO productDTO = new ProductDTO("Presunto",1L,3L,new BigDecimal("300.00"));
        Product product = new Product(null,"Presunto",seller,category,new BigDecimal("300.00"));
        Product savedProduct = new Product(1L,"Presunto",seller,category,new BigDecimal("300.00"));
        ProductWithIdResponse finalProductExpected = new ProductWithIdResponse(1L);
        finalProductExpected.setName("Presunto");
        finalProductExpected.setCategoryId(3L);
        finalProductExpected.setSellerId(1L);
        finalProductExpected.setPrice(new BigDecimal("300.00"));

        when(productRepository.findByName(productDTO.getName())).thenReturn(Optional.empty());
        when(sellerRepository.findById(productDTO.getSellerId())).thenReturn(Optional.of(seller));
        when(categoryRepository.findById(productDTO.getCategoryId())).thenReturn(Optional.of(category));
        when(productRepository.save(product)).thenReturn(savedProduct);

        ProductWithIdResponse finalProductReceived = service.resgisterProduct(productDTO);

        assertEquals(finalProductExpected.getId(),finalProductReceived.getId());
        assertEquals(finalProductExpected.getName(),finalProductReceived.getName());
        assertEquals(finalProductExpected.getPrice(),finalProductReceived.getPrice());
    }

    @Test
    public void shouldReturnUpdatedProduct() {
        ProductDTO productDTO = new ProductDTO("Presunto",1L,3L,new BigDecimal("300.00"));
        Product product = new Product(1L,"Presunto",seller,category,new BigDecimal("300.00"));
        Product savedProduct = new Product(1L,"Presunto",seller,category,new BigDecimal("300.00"));
        ProductWithIdResponse finalProductExpected = new ProductWithIdResponse(1L);
        finalProductExpected.setName("Presunto");
        finalProductExpected.setCategoryId(3L);
        finalProductExpected.setSellerId(1L);
        finalProductExpected.setPrice(new BigDecimal("300.00"));

        when(productRepository.findById(1L)).thenReturn(Optional.of(savedProduct));
        when(sellerRepository.findById(productDTO.getSellerId())).thenReturn(Optional.of(seller));
        when(categoryRepository.findById(productDTO.getCategoryId())).thenReturn(Optional.of(category));
        when(productRepository.save(product)).thenReturn(savedProduct);

        ProductWithIdResponse finalProductReceived = service.updateProduct(productDTO,1L);

        assertEquals(finalProductExpected.getId(),finalProductReceived.getId());
        assertEquals(finalProductExpected.getName(),finalProductReceived.getName());
        assertEquals(finalProductExpected.getPrice(),finalProductReceived.getPrice());
    }

    @Test
    public void shouldReturnProductAlreadyCreatedExceptionOnCreatingProducts() {
        ProductDTO productDTO = new ProductDTO("Presunto",1L,3L,new BigDecimal("300.00"));

        when(productRepository.findByName("Presunto")).thenReturn(Optional.of(product));
        when(sellerRepository.findById(productDTO.getSellerId())).thenReturn(Optional.of(seller));
        when(categoryRepository.findById(productDTO.getCategoryId())).thenReturn(Optional.of(category));

        assertThrows(ProductAlreadyRegistered.class, () -> service.resgisterProduct(productDTO));
    }

    @Test
    public void shouldReturnNotFoundExceptionOnCreatingProduct() {
        ProductDTO productDTO = new ProductDTO("Presunto",1L,3L,new BigDecimal("300.00"));

        when(productRepository.findByName("Presunto")).thenReturn(Optional.empty());
        when(sellerRepository.findById(productDTO.getSellerId())).thenReturn(Optional.empty());
        when(categoryRepository.findById(productDTO.getCategoryId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.resgisterProduct(productDTO));
    }

    @Test
    public void shouldReturnNotFoundExceptionOnUpdatingProducts() {
        ProductDTO productDTO = new ProductDTO("Presunto",1L,3L,new BigDecimal("300.00"));

        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        when(sellerRepository.findById(productDTO.getSellerId())).thenReturn(Optional.empty());
        when(categoryRepository.findById(productDTO.getCategoryId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.updateProduct(productDTO,1L));
    }
}
