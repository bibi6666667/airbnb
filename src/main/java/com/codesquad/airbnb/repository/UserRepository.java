package com.codesquad.airbnb.repository;

import com.codesquad.airbnb.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(User user) {
        String sql = "insert into `user` (`email`, `name`, `access_token`) values (?, ?, ?)";
        jdbcTemplate.update(sql, user.getEmail(), user.getName(), user.getAccessToken());
    }

    public Optional<User> findByEmail(String email) {
        String sql = "select user.id, `name`, `email` from `user` where `email` like ?";
        return jdbcTemplate.query(sql, userRowMapper(), email).stream().findAny();
    }

    private RowMapper<User> userRowMapper() {
        return (resultSet, rowNum) -> {
            User user = new User(resultSet.getLong("id"), resultSet.getString("email"), resultSet.getString("name"));
            return user;
        };
    }
}
