package ru.clevertec.gift_certificates.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.gift_certificates.entity.GiftCertificate;

public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {
}
