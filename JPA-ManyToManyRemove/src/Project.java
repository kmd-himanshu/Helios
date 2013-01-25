
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Project {
    @Id
    protected int id;
    protected String name;
    @ManyToMany(mappedBy="projects")
    private Collection<Student> students;

    public Project() {
        students = new ArrayList<Student>();
    }
    public void setId(int i){
      id = i;
    }
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public Collection<Student> getStudents() {
        return students;
    }
    
    public String toString() {
        return "Project id: " + getId() + ", name: " + getName();
    }
}
