package com.thi.ProductApiTeleAppInsights.controller;

import com.thi.ProductApiTeleAppInsights.model.Product;
import com.thi.ProductApiTeleAppInsights.service.IProductService;
import com.thi.ProductApiTeleAppInsights.telemetry.LoggingConstants;
import com.thi.ProductApiTeleAppInsights.telemetry.TelemetryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")

public class ProductController {
    @Autowired
    private IProductService productService;

    @Autowired
    private TelemetryHelper telemetryHelper;

    @GetMapping
    public List<Product> getAllProducts() {
        //=====================================================//
        // 1. START_PROCESS
        //=====================================================//
        String processName = "getAllProducts";
        TelemetryHelper.logProcess(processName,LoggingConstants.START_PROCESS, null, null);
        List<Product> products = null;
        try {

            // ..............................
            //*** Validation Logic
            // ..............................
            //=====================================================//
            // 2. WARNING_PROCESS
            //=====================================================//
            TelemetryHelper.logProcess(processName, LoggingConstants.WARNING_PROCESS, null, null);

            // ..............................
            //*** Business logic
            // ..............................
            products = productService.findAll();
            //=====================================================//
            // 3. SUCCESS_PROCESS
            //=====================================================//
            TelemetryHelper.logProcess(processName, LoggingConstants.SUCCESS_PROCESS, products, null);
            //telemetryClient.trackTrace("JavaSpringBoot*** End Process: Get All Products", SeverityLevel.Information);

        } catch (Exception ex) {
            //=====================================================//
            // 4. EXCEPTION_PROCESS
            // =====================================================//
            TelemetryHelper.logProcess(processName, LoggingConstants.EXCEPTION_PROCESS, null, ex);
        }

        return products;
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        //=====================================================//
        // 1. START_PROCESS
        //=====================================================//
        Product product = null;
        String processName = "getProductById";
        TelemetryHelper.logProcess(processName, LoggingConstants.START_PROCESS, null, null);

       try {
           // ..............................
           //*** Validation Logic
           // ..............................
           //=====================================================//
           // 2. WARNING_PROCESS
           //=====================================================//
           TelemetryHelper.logProcess(processName, LoggingConstants.WARNING_PROCESS, null, null);

           // ..............................
           //*** Business logic
           // ..............................
            product = productService.findById(id);

           //=====================================================//
           // 3. SUCCESS_PROCESS
           //=====================================================//
            TelemetryHelper.logProcess(processName, LoggingConstants.SUCCESS_PROCESS, null, null);

        } catch (Exception ex) {
           //=====================================================//
           // 4. EXCEPTION_PROCESS
           // =====================================================//
            TelemetryHelper.logProcess(processName, LoggingConstants.EXCEPTION_PROCESS, null, ex);

        }
        return product;
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {

        //=====================================================//
        // 1. START_PROCESS
        //=====================================================//
        String processName = "createProduct";
        TelemetryHelper.logProcess(processName, LoggingConstants.START_PROCESS, product, null);
        Product createdProduct = null;
        try {

            // ..............................
            //*** Validation Logic
            // ..............................
            //=====================================================//
            // 2. WARNING_PROCESS
            //=====================================================//
            TelemetryHelper.logProcess(processName, LoggingConstants.WARNING_PROCESS, null, null);

            // ..............................
            //*** Business logic
            // ..............................
              createdProduct = productService.save(product);
            //=====================================================//
            // 3. SUCCESS_PROCESS
            //=====================================================//
            TelemetryHelper.logProcess(processName, LoggingConstants.SUCCESS_PROCESS, null, null);
        }catch (Exception ex){
            //=====================================================//
            // 4. EXCEPTION_PROCESS
            // =====================================================//
            TelemetryHelper.logProcess(processName, LoggingConstants.EXCEPTION_PROCESS, null, ex);
        }

        return createdProduct;
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {

        //=====================================================//
        // 1. START_PROCESS
        //=====================================================//
        String processName = "deleteProduct";
        TelemetryHelper.logProcess(processName, LoggingConstants.START_PROCESS, null, null);
        try {
            // ..............................
            //*** Validation Logic
            // ..............................
            //=====================================================//
            // 2. WARNING_PROCESS
            //=====================================================//
            TelemetryHelper.logProcess(processName, LoggingConstants.WARNING_PROCESS, null, null);

            // ..............................
            //*** Business logic
            // ..............................
            productService.deleteById(id);
            //=====================================================//
            // 3. SUCCESS_PROCESS
            //=====================================================//
            TelemetryHelper.logProcess(processName, LoggingConstants.SUCCESS_PROCESS, null, null);
        } catch (Exception ex) {
            //=====================================================//
            // 4. EXCEPTION_PROCESS
            // =====================================================//
            TelemetryHelper.logProcess(processName, LoggingConstants.EXCEPTION_PROCESS, null, ex);
        }
    }

}
