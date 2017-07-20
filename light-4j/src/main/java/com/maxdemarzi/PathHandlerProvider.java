package com.maxdemarzi;

import com.networknt.server.HandlerProvider;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

public class PathHandlerProvider implements HandlerProvider {

    private static final File DB_PATH = new File( "target/graph.db" );

    static GraphDatabaseService graphDb  = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );


    @Override
    public HttpHandler getHandler() {
        try(Transaction tx = graphDb.beginTx()) {
            Node node = graphDb.createNode(Label.label("User"));
            node.setProperty("name", "Max De Marzi");
            tx.success();
        }
        return Handlers.path()
                .addExactPath("/json", new JsonGetHandler())
                .addExactPath("/neo4j", new Neo4jGetHandler(graphDb))
                ;
    }
}
