package com.ranjit.todo.todo.repositories;

import com.ranjit.todo.todo.entities.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT e FROM UserEntity e WHERE e.name LIKE %:keyword% OR e.email LIKE %:keyword%")
    List<UserEntity> searchByNameOrEmail(@Param("keyword") String keyword, Pageable pageable);
}
