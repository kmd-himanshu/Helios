
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Professor {

    @Id
    private int id;
    private String name;
    private long salary;

    public Professor() {
    }

    public Professor(int id) {
        this.id = id;
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

    public void setName(String name) {
        this.name = name;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    public String toString() {
        return "Professor id: " + getId() + " name: " + getName() + " salary: " + getSalary();
    }
}
