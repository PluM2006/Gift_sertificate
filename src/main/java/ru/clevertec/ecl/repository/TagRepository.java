package ru.clevertec.ecl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.ecl.entity.Tag;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    @Query(value = "WITH maxprice AS\n" +
            "(SELECT sum(price) AS price, count(*) AS count, t.name, t.id\n" +
            "   FROM orders\n" +
            "       LEFT JOIN certificate_tag ct on orders.certificate_id = ct.certificate_id\n" +
            "           LEFT JOIN users u on u.id = orders.user_id\n" +
            "               LEFT JOIN tag t on t.id = ct.tag_id\n" +
            "WHERE u.username = ?1\n" +
            "GROUP BY t.name, t.id)" +
            "SELECT price, count, name, id\n" +
            "FROM  maxprice\n" +
            "WHERE price = (SELECT max(price) FROM maxprice)\n" +
            "ORDER BY count LIMIT 1", nativeQuery = true)
    Optional<Tag> findPopularTag(String username);

}
