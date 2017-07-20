# embedded_neo4j
Trying different frameworks

In light-4j:

	mvn clean package
	cd target
	java -server -Xms512m -Xmx2g -jar service-1.0-SNAPSHOT.jar
	wrk -t8 -c40 -d30s http://127.0.0.1:8080/neo4j