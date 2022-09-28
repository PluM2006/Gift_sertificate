package ru.clevertec.ecl.exctention;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import ru.clevertec.ecl.dto.CertificateDTO;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class CertificateDtoResolver implements ParameterResolver {

    public static final List<CertificateDTO> CertificateDTOs = Arrays.asList(
            CertificateDTO.builder()
                    .id(1L)
                    .name("Certificate 1")
                    .price(BigDecimal.valueOf(20))
                    .description("The Best 1")
                    .tags(new HashSet<>())
                    .build(),
            CertificateDTO.builder()
                    .id(2L)
                    .name("Certificate 2")
                    .price(BigDecimal.valueOf(30))
                    .description("The Best 2")
                    .tags(new HashSet<>())
                    .build(),
            CertificateDTO.builder()
                    .id(1L)
                    .name("Certificate 3")
                    .price(BigDecimal.valueOf(40))
                    .description("The Best 3")
                    .tags(new HashSet<>())
                    .build()
    );

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == CertificateDTO.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return CertificateDTO.builder()
                .id(1L)
                .name("Certificate 1")
                .price(BigDecimal.valueOf(20))
                .description("The Best 1")
                .tags(new HashSet<>())
                .build();
    }
}
