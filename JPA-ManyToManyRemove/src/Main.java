import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class Main {
  static EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPAService");
  static EntityManager em = emf.createEntityManager();

  public static void main(String[] a) throws Exception {
    em.getTransaction().begin();
    
    
    Student student = new Student();
    student.setId(1);
    em.persist(student);
    
    Project dept = new Project();
    dept.setId(1);
    em.persist(dept);
    
    em.flush();

    em.remove(dept);
    
    Query query = em.createQuery("SELECT e FROM Student e");
    List<Student> list = (List<Student>) query.getResultList();
    System.out.println(list);
    
    query = em.createQuery("SELECT d FROM Project d");
    List<Project> dList = (List<Project>) query.getResultList();
    System.out.println(dList);
    
    em.getTransaction().commit();
    em.close();
    emf.close();
    
    Helper.checkData();
  }
}
