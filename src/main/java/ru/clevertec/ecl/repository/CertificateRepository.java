package ru.clevertec.ecl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.clevertec.ecl.dmain.entity.Certificate;

import java.util.List;
import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    Optional<Certificate> findByName(String name);
    boolean existsByName(String name);

    @Query("SELECT c FROM Certificate c " +
            "inner join c.tags tags " +
            "where (:tag_name is null or tags.name = :tag_name)")



//    @Query("select c from Certificate c inner join c.tags tags where c.id = ?1 and tags.name = ?2")
    List<Certificate> findByIdAndTagsName(
            @Param("tag_name") String q, String s, String ASC, String name);

//    List<Certificate> findAllBy(Example<Certificate> certificateExample);
}
