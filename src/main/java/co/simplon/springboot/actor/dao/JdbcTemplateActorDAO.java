package co.simplon.springboot.actor.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import co.simplon.springboot.actor.model.Actor;

/**
 * JDBD implementation of the Actor DAO interface.
 */
// @Component
@Repository
public class JdbcTemplateActorDAO implements ActorDAO {

	private JdbcTemplate jdbcTemplate;

	/**
	 * Constructor
	 * 
	 * @param jdbcTemplate
	 *            : the JDBCTemplace connected to the Database (thanks to Spring)
	 */
	@Autowired
	public JdbcTemplateActorDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Get the list of all the actors.
	 * 
	 * @return : the list of all the actors.
	 */
	@Override
	public List<Actor> listActors() throws Exception {

		String sql = "SELECT * FROM actor ";

		List<Actor> aLlistOfActor = jdbcTemplate.query(sql, new ResultSetExtractor<List<Actor>>() {
			@Override
			public List<Actor> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Actor> list = new ArrayList<Actor>();
				while (rs.next()) {
					Actor actor = getActorFromResultSet(rs);
					list.add(actor);
				}
				return list;
			}

		});

		return aLlistOfActor;
	}

	/**
	 * Get a specific actor based on ID
	 * 
	 * @param id
	 *            : the id of actor.
	 * @return Actor : the actor object (or null)
	 */
	@Override
	public Actor getActor(Long id) {

		try {
			String sql = "SELECT * FROM actor WHERE actor_id = ?";
			Actor actor = (Actor) jdbcTemplate.queryForObject(sql, new Object[] { id }, new RowMapper<Actor>() {
				@Override
				public Actor mapRow(ResultSet rs, int rowNum) throws SQLException {
					Actor actor = getActorFromResultSet(rs);
					return actor;
				}
			});
			return actor;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

	}

	/**
	 * create a new actor.
	 * 
	 * @param actor
	 *            : the actor information.
	 */
	@Override
	public Actor insertActor(Actor actor) throws Exception {
		String sql = "INSERT INTO actor (actor_id, first_name, last_name, last_update) VALUES (?,?,?,?)";
		Timestamp updateTime = new Timestamp(System.currentTimeMillis());
		long newId;

		actor.setId(new Long(0));

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				int i = 0;
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setLong(++i, actor.getId());
				ps.setString(++i, actor.getFirstName());
				ps.setString(++i, actor.getLastName());
				ps.setTimestamp(++i, updateTime);
				return ps;
			}
		};

		// The newly generated key will be saved in this object
		final KeyHolder holder = new GeneratedKeyHolder();

		jdbcTemplate.update(psc, holder);

		newId = holder.getKey().longValue();
		actor.setId(newId);
		return actor;
	}

	/**
	 * Update an existing actor.
	 * 
	 * @param actor
	 *            : the actor information.
	 */
	@Override
	public Actor updateActor(Actor actor) throws Exception {

		String sql = "UPDATE actor SET first_name = ?, last_name = ?, last_update = ? WHERE actor_id = ?";
		Timestamp updateTime = new Timestamp(System.currentTimeMillis());
		actor.setLastUpdate(updateTime);

		jdbcTemplate.update(sql, new Object[] { actor.getFirstName(), actor.getLastName(), updateTime, actor.getId() });

		return actor;
	}

	/**
	 * Delete an existing actor.
	 * 
	 * @param id
	 *            : the id of actor.
	 */
	@Override
	public void deleteActor(Long id) throws Exception {
		this.jdbcTemplate.update("DELETE FROM actor WHERE actor_id = ?", new Object[] { id });
	}

	/**
	 * Build an actor object with data from the ResultSet
	 * 
	 * @param rs
	 *            : the ResultSet to process.
	 * @return Actor : The new Actor object
	 */
	private Actor getActorFromResultSet(ResultSet rs) throws SQLException {
		Actor actor = new Actor();
		actor.setId(rs.getLong("actor_id"));
		actor.setFirstName(rs.getString("first_name"));
		actor.setLastName(rs.getString("last_name"));
		actor.setLastUpdate(rs.getTimestamp("last_update"));

		return actor;
	}

}
