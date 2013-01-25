package coreservlets;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class CoffeeBean {
  private double numPounds=5;
  
  public double getNumPounds() {
    return(numPounds);
  }

  public void setNumPounds(double numPounds) {
    this.numPounds = numPounds;
  }

  public String showCoffee() {
    return("show-coffee");
  }
}
