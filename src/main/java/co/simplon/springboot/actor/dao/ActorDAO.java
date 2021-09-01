package co.simplon.springboot.actor.dao;

import java.util.List;

import co.simplon.springboot.actor.model.Actor;

/**
 * DAO interface for the actor model.
 */
public interface ActorDAO {

	List<Actor> listActors() throws Exception;

	Actor getActor(Long id) throws Exception;

	Actor insertActor(Actor actor) throws Exception;

	Actor updateActor(Actor actor) throws Exception;
	
	void deleteActor(Long id) throws Exception;

}
