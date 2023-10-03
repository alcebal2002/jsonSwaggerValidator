package com.example;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.model.SimpleRequest;
import com.atlassian.oai.validator.model.SimpleResponse;
import com.atlassian.oai.validator.model.Request;
import com.atlassian.oai.validator.model.Response;
import com.atlassian.oai.validator.report.JsonValidationReportFormat;
import com.atlassian.oai.validator.report.ValidationReport;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SwaggerValidatorDemo {

        private static final String OPENAPI_SPEC_URL = "petstore-openapi.yml";

        public static void main(String[] args) throws IOException, ProcessingException {
                OpenApiInteractionValidator validator = OpenApiInteractionValidator
                                .createForSpecificationUrl(OPENAPI_SPEC_URL)
                                .build();

                final Request request = SimpleRequest.Builder
                                .get("/pet/findByStatus")
                                .build();

                final Response response = SimpleResponse.Builder
                                .ok()
                                .withContentType("application/json")
                                .withBody("[{\"name\":\"Pet1\", \"photoUrls\":[\"url1\"]}]")
                                .build();

                validateRequestResponse(validator, request, response);
        }

        public static void validateRequestResponse(OpenApiInteractionValidator validator, Request request,
                        Response response) {

                ValidationReport validationReport = validator.validate(request, response);

                System.out.println(request.getMethod() + " - " + request.getPath());
                System.out.println("> request : " + request.toString());
                System.out.println("> response: " + response.toString());
                printReportKeys(validationReport);
        }

        public static void printReportKeys(ValidationReport report) {
                if (report.hasErrors()) {
                        System.out.println("Validation Request / Response with ERRORs");
                        System.out.println(JsonValidationReportFormat.getInstance().apply(report));
                } else {
                        System.out.println("Validation Request / Response SUCCESSFUL");
                }
        }

}