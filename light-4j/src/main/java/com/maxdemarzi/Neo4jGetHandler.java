package com.maxdemarzi;

import com.dslplatform.json.*;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import java.nio.ByteBuffer;

public class Neo4jGetHandler implements HttpHandler {
    private DslJson<Object> dsl = new DslJson<>();
    private JsonWriter writer = dsl.newWriter(512);
    private GraphDatabaseService graphDb;

    Neo4jGetHandler(GraphDatabaseService graphDb) {
        this.graphDb = graphDb;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        writer.reset();
        try(Transaction tx = graphDb.beginTx()) {
            Node node = graphDb.getNodeById(0);
            dsl.serialize(writer, new PersonNode(node.getId(), (String)node.getProperty("name")));
            tx.success();
        }

        exchange.getResponseSender().send(ByteBuffer.wrap(writer.toByteArray()));
    }

    static class PersonNode implements JsonObject {
        Long id;
        String name;

        PersonNode(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public void serialize(JsonWriter writer, boolean minimal) {
            writer.writeAscii("{\"id\":");
            NumberConverter.serialize(id, writer);
            writer.writeAscii(",\"name\":");
            StringConverter.serialize(name, writer);
            writer.writeByte(com.dslplatform.json.JsonWriter.OBJECT_END);
        }

    }
}
