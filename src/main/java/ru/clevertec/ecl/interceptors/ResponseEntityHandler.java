package ru.clevertec.ecl.interceptors;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Component
@RequiredArgsConstructor
public class ResponseEntityHandler {

  private final RequestSender requestSender;

  public ResponseEntity<Object> getObjectResponseEntity(ContentCachingRequestWrapper requestWrapper,
                                                        List<Integer> ports) {
    return requestSender.forwardRequest(requestWrapper, ports)
        .stream()
        .limit(1)
        .findFirst()
        .orElseThrow(NoSuchElementException::new);
  }

  public List<ResponseEntity<Object>> getOrderDTOResponseEntity(ContentCachingRequestWrapper requestWrapper,
                                                                  List<Integer> ports) {
    return requestSender.forwardRequest(requestWrapper, ports);
  }

}
