package com.practice.repository;

import com.practice.model.EventModel;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/** EventRepository class. */
@Repository
public class EventRepository {

  private final NamedParameterJdbcTemplate jdbcTemplate;

  @Autowired
  public EventRepository(NamedParameterJdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<EventModel> rowMapper =
      (rs, rowNum) -> {
        EventModel event = new EventModel();
        event.setId(rs.getLong("id"));
        event.setData(rs.getString("data"));
        event.setType(rs.getString("type"));
        event.setProcessed(rs.getBoolean("is_processed"));
        return event;
      };

  /**
   * Find all events.
   *
   * @return list of events
   */
  public List<EventModel> findAll() {
    return jdbcTemplate.query("SELECT * FROM events", rowMapper);
  }

  /**
   * Get unprocessed events.
   *
   * @return list of unprocessed events
   */
  public List<EventModel> getUnprocessed() {
    String sql = "SELECT * FROM events WHERE is_processed = false AND type = :type";
    MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("type", "MailEvent");
    return jdbcTemplate.query(sql, parameters, rowMapper);
  }

  /**
   * Mark event as processed.
   *
   * @param id event id
   */
  public void markAsProcessed(Long id) {
    String sql = "UPDATE events SET is_processed = true WHERE id = :id";
    MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("id", id);
    jdbcTemplate.update(sql, parameters);
  }

  /**
   * Save event.
   *
   * @param event event
   */
  public void save(EventModel event) {
    String sql = "INSERT INTO events (type, data) VALUES (:type, CAST(:data AS JSONB))";

    MapSqlParameterSource parameters =
        new MapSqlParameterSource()
            .addValue("type", event.getType())
            .addValue("data", event.getData());

    jdbcTemplate.update(sql, parameters);
  }
}
