import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class ProfessorService {
  protected EntityManager em;

  public ProfessorService(EntityManager em) {
    this.em = em;
  }

  public void messageCreateAndList() {
    em.persist(new Message("Hello Persistence!"));
    Query q = em.createQuery("select m from Message m");

    for (Message m : (List<Message>) q.getResultList()) {
        System.out.println(m.getMessage()
            + " (created on: " + m.getCreated() + ")"); 
    }
  }

}