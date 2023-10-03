package com.example;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.model.SimpleRequest;
import com.atlassian.oai.validator.model.SimpleResponse;
import com.atlassian.oai.validator.model.Request;
import com.atlassian.oai.validator.model.Response;
import com.atlassian.oai.validator.report.ValidationReport;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SwaggerValidatorDemo {

        private static final String OPENAPI_SPEC_URL = "petstore-openapi.yml";

        private static OpenApiInteractionValidator validator = OpenApiInteractionValidator
                        .createForSpecificationUrl(OPENAPI_SPEC_URL)
                        .build();

        public static void main(String[] args) throws IOException, ProcessingException {
                final Request request = SimpleRequest.Builder
                                .get("/pet/findByStatus")
                                .build();

                final Response response = SimpleResponse.Builder
                                .ok()
                                .withContentType("application/json")
                                .withBody("[{\"name\":\"Pet1\", \"photoUrls\":\"url\"}]")
                                .build();

                validateRequestResponse(request, response);
        }

        public static void validateRequestResponse(Request request, Response response) {
                ValidationReport requestReport = null;
                ValidationReport responseReport = null;

                requestReport = validator.validate(request, response);

                if (request != null)
                        requestReport = validator.validateRequest(request);
                if (response != null)
                        responseReport = validator.validateResponse(request.getPath(),
                                        request.getMethod(), response);

                System.out.println(request.getMethod() + " - " + request.getPath());
                System.out.println("> request:");
                printReportKeys(requestReport);
                System.out.println("> response:");
                printReportKeys(responseReport);
        }

        public static void printReportKeys(ValidationReport report) {
                final List<String> reportKeys = report.getMessages().stream()
                                .map(ValidationReport.Message::getKey)
                                .collect(Collectors.toList());
                reportKeys.forEach(key -> {
                        System.out.println(key);
                });
        }

}