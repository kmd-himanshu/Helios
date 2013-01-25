import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
  public static void main(String[] a) throws Exception {
    JPAUtil util = new JPAUtil();

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProfessorService");
    EntityManager em = emf.createEntityManager();
    ProfessorService service = new ProfessorService(em);

    em.getTransaction().begin();


    service.messageCreateAndList();
    
    util.checkData("select * from Message");

    em.getTransaction().commit();
    em.close();
    emf.close();
  }
}