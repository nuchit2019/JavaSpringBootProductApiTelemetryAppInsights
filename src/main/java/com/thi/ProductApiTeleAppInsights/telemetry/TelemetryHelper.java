package com.thi.ProductApiTeleAppInsights.telemetry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.telemetry.SeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Helper class for telemetry logging using Microsoft Application Insights.
 * Provides a centralized mechanism to log processes, exceptions, and contextual information.
 * Author: Nuchit Atjanawat
 * Create Date: [2024-12-06]
 */
@Component
public class TelemetryHelper {
    // Static variables for accessing environment properties and telemetry client
    private static Environment environment;
    private static TelemetryClient telemetryClient;

    // Logger instance for logging messages to the console
    private static final Logger logger = LoggerFactory.getLogger(TelemetryHelper.class);

    /**
     * Constructor for TelemetryHelper.
     * Initializes the environment and telemetry client instances.
     *
     * @param environment     Spring Environment object to access environment profiles.
     * @param telemetryClient TelemetryClient instance for Application Insights integration.
     */
    @Autowired
    public TelemetryHelper(Environment environment, TelemetryClient telemetryClient) {
        TelemetryHelper.environment = environment;
        TelemetryHelper.telemetryClient = telemetryClient;
    }

    /**
     * Logs the details of a process, including its name, type, context, and any exceptions.
     *
     * @param processName Name of the process being logged.
     * @param logType     Type of log (e.g., START, SUCCESS, WARNING, EXCEPTION).
     * @param context     Additional context information to include in the log.
     * @param ex          Exception to log (if any).
     */
    public static void logProcess(String processName, String logType, Object context, Exception ex) {

        // Ensure the environment is properly initialized
        if (environment == null) {
            throw new IllegalStateException("TelemetryHelper is not initialized. Ensure it is properly configured.");
        }

        // Ensure the telemetry client is properly initialized
        if (telemetryClient == null) {
            logger.error("TelemetryClient is not initialized. Ensure it is configured correctly.");
            return;
        }

        // Create a message template for logging
        String messageTemplate = String.format("%s: %s", logType, processName);

        // Create a TraceTelemetry object for logging process details
        var traceTelemetry = new com.microsoft.applicationinsights.telemetry.TraceTelemetry(messageTemplate, getSeverityLevel(logType));

        // Add context information to telemetry properties (if provided)
        if (context != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                traceTelemetry.getProperties().put("Context", objectMapper.writeValueAsString(context));
            } catch (Exception e) {
                logger.error("Error serializing context", e);
            }
        }

        // Add exception details to telemetry properties (if provided)
        if (ex != null) {
            traceTelemetry.getProperties().put("ExceptionMessage", ex.getMessage());
            StackTraceElement[] stackTrace = ex.getStackTrace();
            if (stackTrace.length > 0) {
                traceTelemetry.getProperties().put("FileName", stackTrace[0].getFileName());
                traceTelemetry.getProperties().put("LineNumber", String.valueOf(stackTrace[0].getLineNumber()));
            }
            telemetryClient.trackException(ex);
        }

        // Send the trace telemetry to Application Insights
        telemetryClient.trackTrace(traceTelemetry);

        // Log the message to the console if the application is running in the "dev" profile
        if (environment.acceptsProfiles("dev")) {
            switch (logType) {
                case LoggingConstants.START_PROCESS:
                case LoggingConstants.SUCCESS_PROCESS:
                    logger.info(messageTemplate);
                    break;
                case LoggingConstants.WARNING_PROCESS:
                    logger.warn(messageTemplate);
                    break;
                case LoggingConstants.EXCEPTION_PROCESS:
                    logger.error(messageTemplate, ex);
                    break;
            }
        }
    }

    /**
     * Determines the appropriate SeverityLevel for the given log type.
     *
     * @param logType The type of log (e.g., START, SUCCESS, WARNING, EXCEPTION).
     * @return The corresponding SeverityLevel for Application Insights.
     */
    private static SeverityLevel getSeverityLevel(String logType) {
        switch (logType) {
            case LoggingConstants.START_PROCESS:
            case LoggingConstants.SUCCESS_PROCESS:
                return SeverityLevel.Information;
            case LoggingConstants.WARNING_PROCESS:
                return SeverityLevel.Warning;
            case LoggingConstants.EXCEPTION_PROCESS:
                return SeverityLevel.Error;
            default:
                return SeverityLevel.Information;
        }
    }
}
