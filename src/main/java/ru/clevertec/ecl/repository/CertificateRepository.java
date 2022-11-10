package ru.clevertec.ecl.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.entity.Certificate;

public interface CertificateRepository extends EntityGraphJpaRepository<Certificate, Long> {

  //  @EntityGraph(attributePaths = {"tags"}, type = EntityGraph.EntityGraphType.LOAD)
  Optional<Certificate> findByName(String name);

  List<Certificate> findByTagsNameIgnoreCaseIsIn(List<String> tagsName, Pageable pageable);

}
