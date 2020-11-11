package co.simplon.springboot.actor.service;

import java.util.List;

import co.simplon.springboot.actor.model.Actor;

public interface ActorService {
	
	public List<Actor> getAllActors() throws Exception;
	public Actor getActor(Long id) throws Exception;
	public Actor addActor(Actor actor) throws Exception;
	public Actor updateActor(Long id, Actor actor) throws Exception;
	public void deleteActor(Long id) throws Exception;
}
