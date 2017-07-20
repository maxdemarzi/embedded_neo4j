package com.maxdemarzi;

import com.networknt.server.ShutdownHookProvider;

public class Neo4jShutdownHookProvider implements ShutdownHookProvider {
    @Override
    public void onShutdown() {
        PathHandlerProvider.graphDb.shutdown();
    }
}
