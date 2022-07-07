package com.example.demoinditex.controller;


import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;
import com.example.demoinditex.dto.Price;
import com.example.demoinditex.service.BranchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/branches")
@Slf4j
@RequiredArgsConstructor
public class BranchController
{

    private final BranchService branchService;


    @Operation(summary = "Get all prices of a branch and product filtered by date")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returns response.", content = @Content(schema = @Schema(implementation = Price.class))),
            @ApiResponse(responseCode = "204", description = "No content."),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/{branchId}/products/{productId}")
    ResponseEntity<Price> getPricesByBranchAndProductFilterByDate(@PathVariable Long branchId,
                                                  @PathVariable  Long productId,
                                                  @RequestParam(required = false) @DateTimeFormat(iso= DATE_TIME) Optional<LocalDateTime> pricingDate)
    {
       log.info("Calling to getPricesByBranchAndProductFilterByDate {} {} {} ",branchId,productId, pricingDate);

       Optional<Price> price;

       if (pricingDate.isPresent())
       {
           price= branchService.getPricesByBranchAndProductFilterByDate(branchId, productId, pricingDate.get());
       }
       else
       {
           price = branchService.getPricesByBranchAndProduct(branchId, productId);

       }

        return price.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));

    }


}
