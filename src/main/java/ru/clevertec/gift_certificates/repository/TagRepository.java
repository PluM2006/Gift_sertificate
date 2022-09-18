package ru.clevertec.gift_certificates.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.gift_certificates.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
