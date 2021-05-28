package com.codesquad.airbnb.repository;

import com.codesquad.airbnb.domain.Wish;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class WishRepository implements JdbcRepository<Wish>{

    private JdbcTemplate jdbcTemplate;

    public WishRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(Long roomId, Long userId) {
        String sql = "insert into `wish` (`room_id`, `user_id`) values (?, ?)";
        jdbcTemplate.update(sql, roomId, userId);
    }

    @Override
    public Optional<Wish> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Wish> findAll() {
        return null;
    }
}