package ru.clevertec.ecl.exctention;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import ru.clevertec.ecl.dmain.entity.Certificate;

import java.math.BigDecimal;
import java.util.HashSet;

public class CertificateResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Certificate.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return Certificate.builder()
                .id(1L)
                .name("Certificate 1")
                .price(BigDecimal.valueOf(20))
                .description("The Best 1")
                .tags(new HashSet<>())
                .build();
    }
}
