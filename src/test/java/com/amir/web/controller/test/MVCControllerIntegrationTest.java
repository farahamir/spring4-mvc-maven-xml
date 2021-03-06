package com.amir.web.controller.test;

import com.amir.web.controller.MVCController;
import com.amir.web.filter.CORSFilter;
import com.amir.web.model.Product;
import com.amir.web.service.ProductService;
import com.amir.web.service.ProductServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.View;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class MVCControllerIntegrationTest {

    @InjectMocks
    private MVCController mvcController;

    @Mock
    private ProductService productService;

    @Mock
    private ModelMap model;

    private MockMvc mockMvc;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mvcController).addFilters(new CORSFilter()).build();
    }

    @Test
    public void testViewProducts() throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("productsView"))
                .andExpect(forwardedUrl("productsView"))
                .andExpect(model().attribute("products", hasSize(2)))
                .andExpect(model().attribute("products", hasItem(
                        allOf(
                                hasProperty("id", is(1)),
                                hasProperty("name", is("car")),
                                hasProperty("catId", is(2)),
                                hasProperty("price", is(500.0))
                        )
                )))
                .andExpect(model().attribute("products", hasItem(
                        allOf(
                                hasProperty("id", is(2)),
                                hasProperty("name", is("phone")),
                                hasProperty("catId", is(1)),
                                hasProperty("price", is(300.0))
                        )
                )));
        verify(productService, times(1)).getProducts();
        verifyNoMoreInteractions(productService);
    }
}