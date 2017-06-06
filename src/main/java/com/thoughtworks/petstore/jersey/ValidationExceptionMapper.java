package com.thoughtworks.petstore.jersey;

import lombok.Data;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.List;
import java.util.stream.Collectors;

public class ValidationExceptionMapper implements ExceptionMapper<javax.validation.ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<FieldError> fields = exception.getConstraintViolations().stream().map(violation -> {
            String field = getFieldName(violation.getPropertyPath().toString());
            return new FieldError(field, violation.getMessage(), getErrorCode(violation.getMessageTemplate()));
        }).collect(Collectors.toList());
        return Response.status(400).entity(fields).build();
    }

    private String getFieldName(String path) {
        String target = path;
        if (path.contains("arg0")) {
            int index = path.indexOf("arg0") + "arg0".length();
            target = path.substring(index);
        }
        return target.split("\\.", 2)[1];
    }

    private String getErrorCode(String template) {
        String content = template.substring(1, template.length() - 1);
        String[] splits = content.split("\\.");
        return splits[splits.length - 2];
    }
}

@Data
class FieldError {
    private String field;
    private String message;
    private String code;

    public FieldError(String field, String message, String code) {
        this.field = field;
        this.message = message;
        this.code = code;
    }
}

