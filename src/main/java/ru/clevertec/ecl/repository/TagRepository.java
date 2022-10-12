package ru.clevertec.ecl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.ecl.entity.Tag;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    @Query(value = "SELECT t.id, t.name, count(*)\n" +
            "FROM orders\n" +
            "         LEFT JOIN certificate_tag ct on orders.certificate_id = ct.certificate_id\n" +
            "         LEFT JOIN users u on u.id = orders.user_id\n" +
            "         LEFT JOIN tag t on t.id = ct.tag_id\n" +
//            "WHERE user_id = (SELECT user_id\n" +
//            "                 FROM orders\n" +
//            "                 GROUP BY user_id\n" +
//            "                 ORDER BY sum(price) desc\n" +
//            "                 LIMIT 1)\n" +
            "GROUP BY t.id, t.name\n" +
            "ORDER BY count(*) desc\n" +
            "LIMIT 1", nativeQuery = true)
    Optional<Tag> findPopularTagUser();
}
