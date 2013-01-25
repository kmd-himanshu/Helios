
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Message {
  @Id
  private long id = System.currentTimeMillis();

  @Basic
  private String message;

  @Basic
  private Date created = new Date();

  public Message() {
  }

  public Message(String msg) {
    message = msg;
  }

  public void setId(long val) {
    id = val;
  }

  public long getId() {
    return id;
  }

  public void setMessage(String msg) {
    message = msg;
  }

  public String getMessage() {
    return message;
  }

  public void setCreated(Date date) {
    created = date;
  }

  public Date getCreated() {
    return created;
  }
}
