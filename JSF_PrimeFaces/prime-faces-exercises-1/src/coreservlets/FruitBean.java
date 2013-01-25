package coreservlets;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class FruitBean {
  private int numApples, numOranges;

  public int getNumApples() {
    return(numApples);
  }

  public void setNumApples(int numApples) {
    this.numApples = numApples;
  }

  public int getNumOranges() {
    return (numOranges);
  }

  public void setNumOranges(int numOranges) {
    this.numOranges = numOranges;
  }
  
  public int getNumFruit() {
    return(numApples + numOranges);
  }

  public String showFruit() {
    return("show-fruit");
  }
}
