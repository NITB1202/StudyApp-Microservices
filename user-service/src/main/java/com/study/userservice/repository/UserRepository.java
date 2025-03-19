package com.study.userservice.repository;

import com.study.userservice.enity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByUsernameIgnoreCase(String username);

    @Query("""
    SELECT u 
    FROM User u
    WHERE u.id IN :ids
      AND (:cursor IS NULL OR u.id > :cursor)
    ORDER BY u.id ASC
    """)
    List<User> findByIdsWithCursor(@Param("ids") List<UUID> ids,
                                   @Param("cursor") UUID cursor,
                                   Pageable pageable);

    @Query("""
    SELECT u
    FROM User u
    WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
      AND (:cursor IS NULL OR u.id > :cursor)
    ORDER BY u.id ASC
    """)
    List<User> searchByUsernameWithCursor(@Param("keyword") String keyword,
                                          @Param("cursor") UUID cursor,
                                          Pageable pageable);

    @Query("""
    SELECT COUNT(u)
    FROM User u
    WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    long countByUsername(@Param("keyword") String keyword);
}