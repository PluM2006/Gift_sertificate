package ru.clevertec.ecl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
