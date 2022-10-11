package ru.clevertec.ecl.repository;

import org.hibernate.criterion.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.entity.Certificate;

import java.util.List;
import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    @EntityGraph(attributePaths = {"tags"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Certificate> findByName(String name);

    List<Certificate> findByTagsNameIgnoreCaseIsIn(List<String> tagsName, Pageable pageable);
}
