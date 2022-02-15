## Table of contents
* [General info](#general-info)
* [Additional applied solutions](#additional_applied_solutions)
* [Run application](#run-application)
* [Next steps](#next-steps)

## General info
This is a recruitment assigment for the Java Developer position at Margo-Group by Mateusz Glanowski.
Time to complete the tasks is about 3 hours including writing this readme.
(I had also a time-consuming (about 30 minutes) trouble creating and running a HyperSQL server on my machine)

## Additional applied solutions
- To reduce the time-consuming operations of connecting to the database, I decided on an approach in which I only write data to the database once. 
It should be checked in further steps whether this approach is right, or it would be better to make more frequent connections saving data to the database;
- Hibernate's creating schema strategy set to "update". It means that schema will automatically create while it doesn't exist.
- I have used Log4j library for info and debug logging;
- I have started writing simple unit tests (68% of service method coverage) but did not cover all possible cases due to the lack of time;
- I have implemented very simple and trivial Multi-threaded solution based on 10 threads ExecutorService to process 
logs read from file. Multi-Threading in this application can be introduced in many ways;
Due to the limited time required to complete the task, I did not manage to test the effectiveness of the 
implemented solution, and the number of threads used;
- To prepare the program for handling very large files (gigabytes) I have used streaming through the file instead of
reading file in memory. Data reading could be further optimized;
	
## Run application
- Download HyperSQL Database Engine (HSQLDB) in version 2.4.0 and unpack it in "C:\" directory,
- Put a server.properties file in "C:\hsqldb-2.4.0\hsqldb" directory. File with properties should contain,
>      server.database.0 = file:hsqldb/demodb
>      server.dbname.0 = testdb
- Create and start a server of HyperSQL Database running below commands in C:\hsqldb-2.4.0\hsqldb,
>      java -classpath lib/hsqldb.jar org.hsqldb.server.Server --database.0
>      java -classpath lib/hsqldb.jar org.hsqldb.server.Server --database.0 file:hsqldb/demodb --dbname.0 testdb
- Run HSQL Database Manager and connect to created database with properties:
>      Type: HSQL Database Engine In-Memory
>      Driver: org.hsqldb.jdbc.JDBCDriver
>      URL: jdbc:hsqldb:hsql://localhost/testdb
>      User: SA
- Run “MargoRecruitAssigmentApplication.class”,
- Make a POST request with a fie that you wanna process as a Request Parameter in Body named file,
	
## Next steps
In the next steps of work on the application I would:
- implement a docker to containerize the application,
- create project documentation in Swagger,
- implement data validation,
- handle exceptions better.