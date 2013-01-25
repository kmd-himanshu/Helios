Example: Shows you how to persist an Entity using JPA

References:
http://www.java2s.com/Code/Java/JPA/PersistAnObject.htm

Environment:
Ant
HSQL DB
NetBeans (Optional)

Steps to run:
1. sart DB:
\data>java -cp ../lib/hsqldb.jar org.hsqldb.Server -database tutorial

2. Start DB Manager:
\data>java -cp ../lib/hsqldb.jar org.hsqldb.util.DatabaseManager

url:
jdbc:hsqldb:hsql://localhost

3. run app:
> ant run