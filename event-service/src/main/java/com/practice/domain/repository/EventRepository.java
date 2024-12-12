package com.practice.domain.repository;

import com.practice.domain.model.EventModel;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * EventRepository
 */
@Repository
public class EventRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public EventRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<EventModel> rowMapper = (rs, rowNum) -> {
        EventModel event = new EventModel();
        event.setId(rs.getLong("id"));
        event.setData(rs.getString("data"));
        event.setType(rs.getString("type"));
        event.setProcessed(rs.getBoolean("is_processed"));
        return event;
    };

    /**
     * Find all events
     *
     * @return List of events
     */
    public List<EventModel> findAll() {
        return jdbcTemplate.query("SELECT * FROM events", rowMapper);
    }

    /**
     * Find all unprocessed events
     *
     * @return List of unprocessed events
     */
    public void save(EventModel event) {
        String sql = "INSERT INTO events (type, data) VALUES (:type, CAST(:data AS JSONB))";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("type", event.getType())
                .addValue("data", event.getData());

        jdbcTemplate.update(sql, parameters);
    }

}
