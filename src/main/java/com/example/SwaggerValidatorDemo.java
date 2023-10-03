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
                String method = "GET";

                validateRequestResponse(openAPISpecUrl, method, requestPath, responseBody);
        }

        public static void validateRequestResponse(String openAPISpecUrl, String stringMethod, String requestPath,
                        String responseBody) {

                Method method = getRequestMethod(stringMethod);

                OpenApiInteractionValidator validator = OpenApiInteractionValidator
                                .createForSpecificationUrl(openAPISpecUrl)
                                .build();

                // final Request request = SimpleRequest.Builder(method, requestPath, false)
                final SimpleRequest.Builder requestBuilder = new SimpleRequest.Builder(method, requestPath, false);
                final Request request = requestBuilder.build();

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