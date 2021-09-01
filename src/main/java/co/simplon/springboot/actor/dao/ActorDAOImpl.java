package co.simplon.springboot.actor.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import co.simplon.springboot.actor.model.Actor;

/**
 * JDBD implementation of the Actor DAO interface.
 */
@Repository
public class ActorDAOImpl implements ActorDAO {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final DataSource datasource;

	/**
	 * Constructor
	 * @param jdbcTemplate : the JDBCTemplace connected to the Database (thanks to Spring)
	 */
	@Autowired
	public ActorDAOImpl(JdbcTemplate jdbcTemplate) {
		this.datasource = jdbcTemplate.getDataSource();
	}

	/**
	 * Get the list of all the actors.
	 * @return : the list of all the actors.
	 */
	@Override
	public List<Actor> listActors() throws Exception {
		Actor actor;
		Connection con = datasource.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs;
		String sql;
		ArrayList<Actor> aListOfActor = new ArrayList<>();

		try {
			// Prepare the SQL query
			sql = "SELECT * FROM actor ";
			pstmt = con.prepareStatement(sql);
			
			// Log info
			logSQL(pstmt);

			// Run the query
			rs = pstmt.executeQuery();

			// Handle the query results
			while (rs.next()) {
				actor = getActorFromResultSet(rs);
				aListOfActor.add(actor);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("SQL Error !:" + pstmt.toString(), e);
			throw e;
		} finally {
			pstmt.close();
			con.close();
		}

		return aListOfActor;
	}

	/**
	 * Get a specific actor based on ID
	 * @param id : the id of actor.
	 * @return Actor : the actor object (or null)
	 */
	@Override
	public Actor getActor(Long id) throws Exception {
		Connection con = datasource.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs;
		Actor actor = null;

		try {
			// Prepare the SQL query
			String sql = "SELECT * FROM actor WHERE actor_id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, id);

			// Log info
			logSQL(pstmt);

			// Run the query
			rs = pstmt.executeQuery();
			
			// Handle the query results
			if (rs.next())
				actor = getActorFromResultSet(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("SQL Error !:" + pstmt.toString(), e);
			throw e;
		} finally {
			pstmt.close();
			con.close();
		}
		return actor;
	}

	/**
	 * create a new actor.
	 * @param actor : the actor information.
	 */
	@Override
	public Actor insertActor(Actor actor) throws Exception {
		Connection con = datasource.getConnection();
		PreparedStatement pstmt = null;
		Actor result = null;
		int i = 0;
		Timestamp updateTime = new Timestamp(System.currentTimeMillis());
		
		try {
			// Prepare the SQL query
			String sql = "INSERT INTO actor (first_name, last_name, last_update) VALUES (?,?,?)";
			pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(++i, actor.getFirstName());
			pstmt.setString(++i, actor.getLastName());
			pstmt.setTimestamp(++i, updateTime);
			
			// Log info
			logSQL(pstmt);
			
			// Run the the update query
			pstmt.executeUpdate();

			// recupération de l'id genere, et maj de l'acteur avec l'id et la date de modif
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				actor.setId(rs.getLong(1));
				actor.setLastUpdate(updateTime);
				
				result = actor;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("SQL Error !:" + pstmt.toString(), e);
			throw e;
		} finally {
			pstmt.close();
			con.close();
		}
		
		return result;

	}

	/**
	 * Update an existing actor.
	 * @param actor : the actor information.
	 */
	@Override
	public Actor updateActor(Actor actor) throws Exception {
		Connection con = datasource.getConnection();
		Actor result;
		PreparedStatement pstmt = null;
		int i = 0;
		Timestamp updateTime = new Timestamp(System.currentTimeMillis());
				
		try {
			// Prepare the SQL query
			String sql = "UPDATE actor SET first_name = ?, last_name = ?, last_update = ? WHERE actor_id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(++i, actor.getFirstName());
			pstmt.setString(++i, actor.getLastName());
			pstmt.setTimestamp(++i, updateTime);
			pstmt.setLong(++i, actor.getId());
			
			// Log info
			logSQL(pstmt);
			
			// Run the the update query
			int resultCount = pstmt.executeUpdate();
			if(resultCount != 1)
				throw new Exception("Actor not found !");
			
			actor.setLastUpdate(updateTime);
			result = actor;

		} catch (SQLException e) {
			e.printStackTrace();
			log.error("SQL Error !:" + pstmt.toString(), e);
			throw e;
		} finally {
			pstmt.close();
			con.close();
		}

		return result;
	}

	/**
	 * Delete an existing actor.
	 * @param id : the id of actor.
	 */
	@Override
	public void deleteActor(Long id) throws Exception {
		Connection con = datasource.getConnection();
		PreparedStatement pstmt = null;
		
		try {
			// Prepare the SQL query
			String sql = "DELETE FROM actor WHERE actor_id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, id);
			
			// Log info
			logSQL(pstmt);
			
			// Run the the update query
			int result = pstmt.executeUpdate();
			if(result != 1)
				throw new Exception("Actor not found !");
			
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("SQL Error !:" + pstmt.toString(), e);
			throw e;
		} finally {
			pstmt.close();
			con.close();
		}

	}

	/**
	 * Build an actor object with data from the ResultSet
	 * @param rs : the ResultSet to process.
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

	/**
	 * Debug function used to log information on the database requests
	 * @param pstmt : The PreparedStatement.
	 */
	private void logSQL(PreparedStatement pstmt) {
		String sql;
		
		if (pstmt == null)
			return;
		
		sql = pstmt.toString().substring(pstmt.toString().indexOf(":") + 2);
		log.debug(sql);
	}
}
