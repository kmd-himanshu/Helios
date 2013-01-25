import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Student {
    @Id
    private int id;
    private String name;

    
    @ManyToMany 
    private Collection<Project> projects;

    public Student() {
        projects = new ArrayList<Project>();
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    
    public Collection<Project> getProjects() {
        return projects;
    }
    
    public String toString() {
        return "Student " + getId() + 
               ": name: " + getName() ;
    }
}
