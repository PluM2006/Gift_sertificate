package ru.clevertec.ecl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.entity.GiftCertificate;

public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {
}
