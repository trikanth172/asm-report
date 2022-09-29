# ASM REPORT
```
The main aim of this project is to gather the asm report statistics of various databases 
```

## Below steps are used to generate the executable jar file in target/ folder 
```
mvn compile -f pom.xml 
mvn install -DskipTests
mvn package
```

### The jar is generated we need to execute like below 

```java
java -jar asm-report.jar --logdir log/
```
Note : the log directory can be any directory but while executing we need to give the full path for --logdir argument 

After execution the asm_report.html is generated in the specified --logdir argument folder.
