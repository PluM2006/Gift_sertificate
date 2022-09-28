package ru.clevertec.ecl.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.clevertec.ecl.entity.Certificate;

import java.util.List;
import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    Optional<Certificate> findByName(String name);

    boolean existsByName(String name);

    @Query("SELECT c, t FROM Certificate c " +
            "LEFT JOIN c.tags t " +
            "WHERE (c.description LIKE CONCAT('%',:description,'%') or :description IS null )" +
            "AND (t.name = :tagName OR :tagName IS null)")
    List<Certificate> findByTagNameDescription(
            @Param("tagName") String tagName,
            @Param("description") String description,
            Pageable pageable);

}
