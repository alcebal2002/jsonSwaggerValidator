package com.example;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.model.SimpleRequest;
import com.atlassian.oai.validator.model.SimpleResponse;
import com.atlassian.oai.validator.model.Request.Method;
import com.atlassian.oai.validator.model.Request;
import com.atlassian.oai.validator.model.Response;
import com.atlassian.oai.validator.report.JsonValidationReportFormat;
import com.atlassian.oai.validator.report.ValidationReport;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import java.io.IOException;

public class SwaggerValidatorDemo {

        public static void main(String[] args) throws IOException, ProcessingException {

                String openAPISpecUrl = "petstore-openapi.yml";
                String requestPath = "/pet/findByStatus";
                String responseBody = "[{\"name\":\"Pet1\", \"photoUrls\":[\"url1\"]}]";
                Method method = Method.GET;

                validateRequestResponse(openAPISpecUrl, method, requestPath, responseBody);
        }

        public static void validateRequestResponse(String openAPISpecUrl, Method method, String requestPath,
                        String responseBody) {

                OpenApiInteractionValidator validator = OpenApiInteractionValidator
                                .createForSpecificationUrl(openAPISpecUrl)
                                .build();

                final Request request = SimpleRequest.Builder
                                .get(requestPath)
                                .build();

                final Response response = SimpleResponse.Builder
                                .ok()
                                .withContentType("application/json")
                                .withBody(responseBody)
                                .build();

                System.out.println(request.getMethod() + " - " + request.getPath());
                System.out.print("> Request : ");
                printValidationReport(validator.validateRequest(request));
                System.out.print("> Response: ");
                printValidationReport(validator.validateResponse(requestPath, method, response));
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