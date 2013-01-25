package coreservlets;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class SuperBowlBean {
  private int numTickets=6;

  public int getNumTickets() {
    return(numTickets);
  }

  public void setNumTickets(int numTickets) {
    this.numTickets = numTickets;
  }
  
  public String showTickets() {
    return("show-tickets");
  }
}
