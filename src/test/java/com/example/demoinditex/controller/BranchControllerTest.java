package com.example.demoinditex.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.times;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.example.demoinditex.Util;
import com.example.demoinditex.dto.Price;
import com.example.demoinditex.service.BranchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BranchController.class)
public class BranchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BranchService branchService;

    @Test
    void whenGetUsingBranchIdAndProductIdPathParameters_thenReturns200() throws Exception {

        Price price = Util.generatePrice();
        Optional<Price> optionalPrice = Optional.of(price);

        when(branchService.getPricesByBranchAndProduct(1L,1L)).thenReturn(optionalPrice);

        mockMvc.perform(get("/api/v1/branches/{branchId}/products/{productId}",1,1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(optionalPrice)));

        verify(branchService, times(1)).getPricesByBranchAndProduct(1L,1L);
    }

    @Test
    void whenGetUsingBranchIdAndProductIdPathParametersAndDate_thenReturns200() throws Exception {

        Price price = Util.generatePrice();
        Optional<Price> optionalPrice = Optional.of(price);

        when(branchService.getPricesByBranchAndProductFilterByDate(1L,1L, LocalDateTime.of(2015, 12, 30, 0, 0, 0))).thenReturn(optionalPrice);

        mockMvc.perform(get("/api/v1/branches/{branchId}/products/{productId}",1,1).
                        param("pricingDate","2015-12-30T00:00:00.000Z"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(optionalPrice)));

        verify(branchService, times(1)).getPricesByBranchAndProductFilterByDate(1L,1L, LocalDateTime.of(2015, 12, 30, 0, 0, 0));
    }

    @Test
    void whenGetUsingBranchIdAndProductIdPathParametersAndDateBadRequest_thenReturns400() throws Exception {

        mockMvc.perform(get("/api/v1/branches/{branchId}/products/{productId}",1,1).
                        param("pricingDate","2015-123123.000Z"))
                .andExpect(status().isBadRequest());

        verify(branchService, times(0)).getPricesByBranchAndProductFilterByDate(1L,1L, LocalDateTime.of(2015, 12, 30, 0, 0, 0));
    }

    @Test
    void whenGetUsingBranchIdAndProductIdPathParametersAndDateWrongEndpoint_thenReturns404() throws Exception {

        mockMvc.perform(get("/api/v1/branchess/{branchId}/products/{productId}",1,1).
                        param("pricingDate","2015-123123.000Z"))
                .andExpect(status().isNotFound());

        verify(branchService, times(0)).getPricesByBranchAndProductFilterByDate(1L,1L, LocalDateTime.of(2015, 12, 30, 0, 0, 0));
    }

}
