

# Project Overview

This project integrates telemetry logging, process tracking, and application insights to enhance monitoring and debugging capabilities for a product API. Below is a summary of the key components:

## Summary of Classes
- **LoggingConstants**
- **TelemetryHelper**

### **Class LoggingConstants**
- **Purpose**:  
  Provides constant values used for defining different log types in the application. These constants are utilized for categorizing log messages (e.g., start, success, warning, exception).
- **Key Features**:
    - Centralized management of log type identifiers.
    - Ensures consistency across the application for log categorization.



### **Class TelemetryHelper**
- **Purpose**:  
  A utility class to log telemetry data using Microsoft Application Insights. It tracks process flows, exceptions, and contextual information for debugging and analytics.
- **Key Features**:
    - Logs process events with severity levels (Information, Warning, Error).
    - Tracks exceptions with detailed context, including file name and line number.
    - Serializes and includes additional context data for telemetry.
    - Supports environment-specific logging (e.g., enhanced logging in the `dev` profile).

---

### **Class ProductController**
- **Purpose**:  
  Manages product-related HTTP API endpoints. Responsible for handling incoming requests, processing data, and sending responses to the client.
- **Key Features**:
    - Implements CRUD operations for managing products.
    - Uses `TelemetryHelper` for logging and monitoring API activity.
    - Provides robust error handling and informative responses for clients.

---

## Getting Started

1. **Prerequisites**:
    - Java 8+
    - Microsoft Application Insights SDK integrated.
    - Spring Boot framework.

2. **Setup**:
    - Clone the repository and configure your `application.properties` or `application.yml` with the required Application Insights settings and database configurations.
    - Build the project using Maven or Gradle.

## Add dependency
 ```markdown
    <dependency>
        <groupId>com.microsoft.azure</groupId>
	    <artifactId>applicationinsights-spring-boot-starter</artifactId>
        <version>2.6.4</version>
    </dependency>
 ```

## Config: application.properties
 ```markdown
    # Application Insights
    applicationinsights.connection.string=YOUR_CONNECTION_STRING
 ```

## Flow Code Logging Example...
 ```markdown
//=====================================================//
// 1. START_PROCESS
//=====================================================//

// ..............................
//*** Validation Logic
// ..............................
//=====================================================//
// 2. WARNING_PROCESS
//=====================================================//

// ..............................
//*** Business logic
// ..............................
//=====================================================//
// 3. SUCCESS_PROCESS
//=====================================================//

//=====================================================//
// 4. EXCEPTION_PROCESS
//=====================================================//

 ```
## ProductController
 ``` Java
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
        TelemetryHelper.logProcess(processName, LoggingConstants.START_PROCESS, null, null);
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
            TelemetryHelper.logProcess(processName, LoggingConstants.SUCCESS_PROCESS, null, null);
            //telemetryClient.trackTrace("JavaSpringBoot*** End Process: Get All Products", SeverityLevel.Information);

        } catch (Exception ex) {
            //=====================================================//
            // 4. EXCEPTION_PROCESS
            // =====================================================//
            TelemetryHelper.logProcess(processName, LoggingConstants.EXCEPTION_PROCESS, null, ex);
        }

        return products;
    }

 // ... other controller methods
 
}
 ```

3. **Run**:  
   Start the Spring Boot application and access the API via the provided endpoints.

4. **Monitoring**:  
   Check telemetry logs in Application Insights for detailed tracking and debugging of processes and API calls.
   ![image](https://github.com/user-attachments/assets/7b5a762a-7e8b-45b8-9b06-977164cbf47f)


---
