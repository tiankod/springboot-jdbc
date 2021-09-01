package co.simplon.springboot.actor.service;

import java.util.List;

import co.simplon.springboot.actor.model.Actor;

public interface ActorService {
	
	List<Actor> getAllActors() throws Exception;
	Actor getActor(Long id) throws Exception;
	Actor addActor(Actor actor) throws Exception;
	Actor updateActor(Long id, Actor actor) throws Exception;
	void deleteActor(Long id) throws Exception;
}
