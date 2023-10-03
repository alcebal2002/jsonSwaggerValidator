package com.example;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.model.SimpleRequest;
import com.atlassian.oai.validator.model.SimpleResponse;
import com.atlassian.oai.validator.model.Request.Method;
import com.atlassian.oai.validator.model.Request;
import com.atlassian.oai.validator.model.Response;
import com.atlassian.oai.validator.report.JsonValidationReportFormat;
import com.atlassian.oai.validator.report.ValidationReport;
import java.io.IOException;

public class SwaggerValidatorDemo {

        public static void main(String[] args) throws IOException {

                String openAPISpecUrl = "petstore-openapi.yml";
                String requestPath = "/pet/findByStatus";
                int responseStatus = 200;
                String responseBody = "[{\"name\":\"Pet1\", \"photoUrls\":[\"url1\"]}]";
                String method = "GET";
                String contentType = "application/json";

                validateRequestResponse(openAPISpecUrl, method, contentType, requestPath, responseStatus, responseBody);
        }

        public static void validateRequestResponse(String openAPISpecUrl, String stringMethod, String contentType,
                        String requestPath,
                        int responseStatus,
                        String responseBody) {

                Method method = getRequestMethod(stringMethod);

                OpenApiInteractionValidator validator = OpenApiInteractionValidator
                                .createForSpecificationUrl(openAPISpecUrl)
                                .build();

                final SimpleRequest.Builder requestBuilder = new SimpleRequest.Builder(method, requestPath, false);
                final Request request = requestBuilder.build();

                final SimpleResponse.Builder responseBuilder = new SimpleResponse.Builder(responseStatus);
                if (contentType != null)
                        responseBuilder.withContentType(contentType);
                if (responseBody != null)
                        responseBuilder.withBody(responseBody, null);
                final Response response = responseBuilder.build();

                System.out.println(request.getMethod() + " - " + request.getPath());
                System.out.print("> Request : ");
                printValidationReport(validator.validateRequest(request));
                System.out.print("> Response: ");
                printValidationReport(validator.validateResponse(requestPath, method, response));
        }

        public static Method getRequestMethod(String method) {
                switch (method) {
                        case "GET":
                                return Method.GET;
                        case "POST":
                                return Method.POST;
                        case "PATCH":
                                return Method.PATCH;
                        case "DELETE":
                                return Method.DELETE;
                        case "HEAD":
                                return Method.HEAD;
                        case "OPTIONS":
                                return Method.OPTIONS;
                        case "TRACE":
                                return Method.TRACE;
                        default:
                                return Method.GET;
                }

        }

        public static String printValidationReport(ValidationReport report) {
                if (report.hasErrors()) {
                        System.out.println("ERROR");
                        System.out.println(JsonValidationReportFormat.getInstance().apply(report));
                        return "ERROR";
                } else {
                        System.out.println("SUCCESS");
                        return "SUCCESS";
                }
        }
}