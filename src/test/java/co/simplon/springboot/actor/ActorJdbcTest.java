package co.simplon.springboot.actor;

import co.simplon.springboot.actor.dao.ActorDAO;
import co.simplon.springboot.actor.model.Actor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActorApplication.class)

public class ActorJdbcTest {

	@Autowired
	ActorDAO actorDAO;



	@Test
	public void testFindOneOk() {
		
		Actor actor = new Actor();
		
		try {
			actor = actorDAO.getActor((long) 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals("PENELOPE", actor.getFirstName());
	}

	@Test
	public void testFindOneKo() {
		Actor actor = new Actor();

		try {
			actor = actorDAO.getActor((long) 30052122);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNull(actor);
	}

	@Test
	public void testFindOneBisOk() {
		Actor actor = new Actor();

		try {
			actor = actorDAO.getActor((long) 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertThat(actor, instanceOf(Actor.class));
	}

	@Test
	public void testInsert() {
		new Actor();
		Actor actor;
		Actor actorNew = null;

		try {
			actor = createMock("Jean", "saisrien");
			actorNew = actorDAO.insertActor(actor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(actorNew);
	}

	@Test
	public void testUpdate() {
		Actor actor = new Actor();
		Actor actorNew;

		actorNew = null;
		actor.setFirstName("Jack");
		actor.setLastName("Ouille");
		actor.setId((long) 2);
		actor.setLastUpdate(new Timestamp(System.currentTimeMillis()));
		try {
			actorNew = actorDAO.updateActor(actor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(actorNew);
		assertEquals("Jack", actorNew.getFirstName());

	}
	
	@Test
	public void testDelete() {
		Actor actor = new Actor();
		Long id = (long) 3;

		try {
			actorDAO.deleteActor(id);
			actor = actorDAO.getActor(id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertNull(actor);
	}

	private Actor createMock(String firstName, String lastName) {
		Actor mock = new Actor();
		mock.setFirstName(firstName);
		mock.setLastName(lastName);
		mock.setId(0L);
		mock.setLastUpdate(new Timestamp(System.currentTimeMillis()));
		return mock;
	}

}
