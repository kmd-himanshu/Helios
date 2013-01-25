package org.bcje.model;
import java.sql.*;
import java.util.*;
public class TableBean {

Connection con ;
Statement ps;
ResultSet rs;
private List perInfoAll = new ArrayList(); 

public List getperInfoAll() {
 int i = 0;
	  try
	  {
		  
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/boomerdb","boomer","ssn!593");
	     ps = con.createStatement();
		 rs = ps.executeQuery("select * from DEVICEMODEL");
		while(rs.next())
		  {
			System.out.println(rs.getString(1));
			perInfoAll.add(i,new perInfo(rs.getString(1),rs.getString(2),rs.getString(3)));
			i++;

		  }
		  
	  }
	  catch (Exception e)
	  {
		  System.out.println("Error Data : " + e.getMessage());
	  }
return perInfoAll;
}


public class perInfo {

String uname;
String firstName;
String lastName;


public perInfo(String firstName,String lastName,String uname) {
this.uname = uname;
this.firstName = firstName;
this.lastName = lastName;

}

public String getUname() {
return uname;
}

public String getFirstName() {
return firstName;
}

public String getLastName() {
return lastName;
}

}

}