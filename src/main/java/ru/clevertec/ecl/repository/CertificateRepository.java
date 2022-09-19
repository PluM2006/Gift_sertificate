package ru.clevertec.ecl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.entity.Certificate;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
}
