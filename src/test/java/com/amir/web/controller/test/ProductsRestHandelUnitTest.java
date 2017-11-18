package com.amir.web.controller.test;

import com.amir.web.controller.ProductsRestHandel;
import com.amir.web.filter.CORSFilter;
import com.amir.web.model.Product;
import com.amir.web.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class ProductsRestHandelUnitTest {
    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductsRestHandel productsController;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productsController).addFilters(new CORSFilter()).build();
    }

    @Test
    public void test_get_all_products_success() throws Exception {
        List<Product> productList = Arrays.asList(
            new Product(1,"car",2,500.0),
                new Product(2,"phone",1,300.0)
        );
        when(productService.getProducts()).thenReturn(productList);
        mockMvc.perform(get("/rest/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("car")))
                .andExpect(jsonPath("$[0].catId", is(2)))
                .andExpect(jsonPath("$[0].price", is(500.0)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("phone")))
                .andExpect(jsonPath("$[1].catId", is(1)))
                .andExpect(jsonPath("$[1].price", is(300.0)));
        verify(productService, times(1)).getProducts();
        verifyNoMoreInteractions(productService);

    }

    @Test
    public void test_get_by_id_success() throws Exception {
        Product product = new Product(1, "amir",2,300.0);

        when(productService.findById(1)).thenReturn(product);

        mockMvc.perform(get("//{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("amir")))
                .andExpect(jsonPath("$.catId", is(2)))
                .andExpect(jsonPath("$.price", is(300.0)));

        verify(productService, times(1)).findById(1);
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void test_get_by_id_fail_404_not_found() throws Exception {
        when(productService.findById(1)).thenReturn(new Product());

        mockMvc.perform(get("/rest/api/products/{id}", 1))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).findById(1);
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void test_add_product() throws Exception {
        Product product = new Product(0,"amir",2,300.0);

        when(productService.exists(product)).thenReturn(false);
        when(productService.addProduct(product)).thenReturn(0);

        mockMvc.perform(
                post("/rest/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(asJsonString(product)))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("/api/products/0")));

        verify(productService, times(1)).exists(product);
        verify(productService, times(1)).addProduct(product);
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void test_add_product_fail_404_not_found() throws Exception {
        Product product = new Product(1,"amir",2,300.0);

        when(productService.exists(product)).thenReturn(true);

        mockMvc.perform(
                post("/rest/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(product)))
                .andExpect(status().isConflict());

        verify(productService, times(1)).exists(product);
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void test_update_product() throws Exception {
        Product product = new Product(1,"amir",2,300.0);
        when(productService.findById(product.getId())).thenReturn(product);
        doNothing().when(productService).updateProduct(product.getId(),product);
        mockMvc.perform(
                put("/rest/api/products/{id}",product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(product)))
                .andExpect(status().isOk());
        verify(productService,times(1)).findById(product.getId());
        verify(productService,times(1)).updateProduct(product.getId(),product);
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void test_update_product_fail_404_not_found() throws Exception {
        Product product = new Product(999,"amir",2,300.0);
        when(productService.findById(product.getId())).thenReturn(new Product());
        doNothing().when(productService).updateProduct(product.getId(),product);
        mockMvc.perform(
                put("/rest/api/products/{id}",product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(product)))
                .andExpect(status().isNotFound());
        verify(productService,times(1)).findById(product.getId());
        verifyNoMoreInteractions(productService);
    }


    @Test
    public void test_remove_product() throws Exception {

        Product product = new Product(1,"amir",2,300.0);
        when(productService.findById(product.getId())).thenReturn(product);
        doNothing().when(productService).deleteProduct(product.getId());
        mockMvc.perform(
                delete("/rest/api/products/{id}",product.getId()))
                .andExpect(status().isOk());
        verify(productService,times(1)).findById(product.getId());
        verify(productService,times(1)).deleteProduct(product.getId());
        verifyNoMoreInteractions(productService);

    }

    @Test
    public void test_remove_product_fail_404_not_found() throws Exception {

        Product product = new Product(999,"amir",2,300.0);
        when(productService.findById(product.getId())).thenReturn(new Product());
        mockMvc.perform(
                delete("/rest/api/products/{id}",product.getId()))
                .andExpect(status().isNotFound());
        verify(productService,times(1)).findById(product.getId());
        verifyNoMoreInteractions(productService);

    }

    // =========================================== CORS Headers ===========================================

    @Test
    public void test_cors_headers() throws Exception {
        mockMvc.perform(get("/rest/api/products"))
                .andExpect(header().string("Access-Control-Allow-Origin", "*"))
                .andExpect(header().string("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE"))
                .andExpect(header().string("Access-Control-Allow-Headers", "*"))
                .andExpect(header().string("Access-Control-Max-Age", "3600"));
    }
    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}