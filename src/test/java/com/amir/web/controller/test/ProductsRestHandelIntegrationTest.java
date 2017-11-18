package com.amir.web.controller.test;

import com.amir.web.model.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static junit.framework.TestCase.fail;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;


@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class ProductsRestHandelIntegrationTest {

    private static final String BASE_URI = "http://localhost:8080/spring4/rest/api/products";
    private static final int UNKNOWN_ID = Integer.MAX_VALUE;

    @Autowired
    private RestTemplate template;


    @Test
    public void test_get_all_success(){
        ResponseEntity<Product[]> response = template.getForEntity(BASE_URI, Product[].class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().length, greaterThanOrEqualTo(0));
        //validateCORSHttpHeaders(response.getHeaders());
    }

    @Test
    public void test_get_by_id_success(){
        ResponseEntity<Product> response = template.getForEntity(BASE_URI + "/36", Product.class);
        Product product = response.getBody();
        assertThat(product.getId(), is(36));
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        //validateCORSHttpHeaders(response.getHeaders());
    }

    @Test
    public void test_get_by_id_failure_not_found(){
        try {
            ResponseEntity<Product> response = template.getForEntity(BASE_URI + "/" + UNKNOWN_ID, Product.class);
            fail("should return 404 not found");
        } catch (HttpClientErrorException e){
            assertThat(e.getStatusCode(), is(HttpStatus.NOT_FOUND));
            //validateCORSHttpHeaders(e.getResponseHeaders());
        }
    }

    // =========================================== Create New User ========================================

    @Test
    public void test_create_new_user_success(){
        Product product = new Product(0,"product_test",2,0);
        URI location = template.postForLocation(BASE_URI, product, Product.class);
        assertThat(location, notNullValue());
        int productToDelete = Integer.parseInt(location.toString().split("/")[location.toString().split("/").length-1]);
        template.delete(BASE_URI+"/"+productToDelete);
    }



    @Test
    public void test_create_new_user_fail_exists(){
        Product existingUser = new Product(36,"Arya Stark",2,0);
        try {
            URI location = template.postForLocation(BASE_URI, existingUser, Product.class);
            fail("should return 409 conflict");
        } catch (HttpClientErrorException e){
            assertThat(e.getStatusCode(), is(HttpStatus.CONFLICT));
            //validateCORSHttpHeaders(e.getResponseHeaders());
        }
    }

    // =========================================== Update Existing User ===================================

    @Test
    public void test_update_user_success(){
        Product existingUser = new Product(36, "amir",1,1200);
        template.put(BASE_URI + "/" + existingUser.getId(), existingUser);
    }

    @Test
    public void test_update_user_fail(){
        Product existingUser = new Product(UNKNOWN_ID, "update",2,0);
        try {
            template.put(BASE_URI + "/" + existingUser.getId(), existingUser);
            fail("should return 404 not found");
        } catch (HttpClientErrorException e){
            assertThat(e.getStatusCode(), is(HttpStatus.NOT_FOUND));
            //validateCORSHttpHeaders(e.getResponseHeaders());
        }
    }

    // =========================================== Delete User ============================================

    @Test
    public void test_delete_user_success(){
        template.delete(BASE_URI + "/" + getLastUser().getId());
    }

    @Test
    public void test_delete_user_fail(){
        try {
            template.delete(BASE_URI + "/" + UNKNOWN_ID);
            fail("should return 404 not found");
        } catch (HttpClientErrorException e){
            assertThat(e.getStatusCode(), is(HttpStatus.NOT_FOUND));
            //validateCORSHttpHeaders(e.getResponseHeaders());
        }
    }

    private Product getLastUser(){
        ResponseEntity<Product[]> response = template.getForEntity(BASE_URI, Product[].class);
        Product[] products = response.getBody();
        return products[products.length - 1];
    }
    public void validateCORSHttpHeaders(HttpHeaders headers){
        assertThat(headers.getAccessControlAllowOrigin(), is("*"));
        assertThat(headers.getAccessControlAllowHeaders(), hasItem("*"));
        assertThat(headers.getAccessControlMaxAge(), is(3600L));
        assertThat(headers.getAccessControlAllowMethods(), hasItems(
                HttpMethod.GET,
                HttpMethod.POST,
                HttpMethod.PUT,
                HttpMethod.OPTIONS,
                HttpMethod.DELETE));
    }
}
