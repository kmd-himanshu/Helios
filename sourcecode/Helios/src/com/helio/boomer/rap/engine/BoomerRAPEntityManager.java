package com.helio.boomer.rap.engine;

import javax.persistence.*;

@Deprecated
public class BoomerRAPEntityManager {

  private EntityManagerFactory emf;

  private BoomerRAPEntityManager() {
      try {
          initializeConnection();
      } catch (BoomerRAPException e) {
          e.printStackTrace();
      }
  }
  
  private static class BoomerRAPEntityManagerHolder {
      private static final BoomerRAPEntityManager INSTANCE = new BoomerRAPEntityManager();
  }
  
  public static BoomerRAPEntityManager getInstance() {
      return BoomerRAPEntityManagerHolder.INSTANCE;
  }
  
  //=========================================================================
  //== Main Controller classes
  //=========================================================================
  
  public void initializeConnection() throws BoomerRAPException {
      try {
          emf = Persistence.createEntityManagerFactory("boomerdb");
      } catch (Exception e) {
          throw new BoomerRAPException();
      }
  }
  
  public EntityManagerFactory getEntityManagerFactory() {
      return emf;
  }
  
  public EntityManager getNewEntityManager() {
      return emf.createEntityManager();
  }
  
}
