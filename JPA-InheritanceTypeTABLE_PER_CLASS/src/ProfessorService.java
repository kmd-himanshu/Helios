import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class ProfessorService {
  protected EntityManager em;

  public ProfessorService(EntityManager em) {
    this.em = em;
  }

  public void createProfessor(Professor emp) {
    em.persist(emp);
  }

  public Collection<Professor> findAllProfessors() {
    Query query = em.createQuery("SELECT e FROM Professor e");
    return (Collection<Professor>) query.getResultList();
  }
}