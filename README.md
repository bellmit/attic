# Glassfish 4.0 JNDI JDBC lookup bug

[The documentation](https://glassfish.java.net/docs/4.0/application-deployment-guide.pdf) claims:

> If a web application accesses a `DataSource` that is not specified
> in a `resource-ref` in `glassfish-web.xml`, or there is no
> `glassfish-web.xml` file, the `resource-ref-name` defined in `web.xml`
> is used. A warning message is logged, recording the JNDI name that was
> used to look up the resource.

This was true in Glassfish 3.x, but in Glassfish 4.0, it always maps
`DataSource`s to `jdbc/__default`.

## Reproduction case:

    # db setup
    ## Set password to 'glassfish'
    createuser \
        --no-superuser \
        --no-createrole \
        --no-createdb \
        --pwprompt \
        glassfish
    createdb \
        --owner glassfish \
        glassfish
    # domain setup
    ## Accept default admin password (no password/'admin' username)
    asadmin create-domain jndi-example
    cp \
        postgresql-9.1-901-1.jdbc4.jar \
        $GLASSFISH/glassfish/domains/jndi-example/lib/
    asadmin start-domain jndi-example
    asadmin create-jdbc-connection-pool \
        --restype javax.sql.DataSource \
        --datasourceclassname org.postgresql.ds.PGSimpleDataSource \
        --transactionisolationlevel read-committed \
        --steadypoolsize 0 \
        --isconnectionvalidationrequired true \
        --connectionvalidationmethod meta-data \
        --failallconnections true \
        --allownoncomponentcallers true \
        --ping true \
        --property "User=glassfish:Password=glassfish:DatabaseName=glassfish:ServerName=localhost" \
        ExamplePool
    asadmin create-jdbc-resource \
        --connectionpoolid ExamplePool \
        jdbc/example
    # deploy demo app
    mvn clean package
    asadmin deploy \
        --upload \
        --contextroot / \
        target/jndi-jdbc-bug-1.0-SNAPSHOT.war
    # run it
    curl http://localhost:8080/lookup/

### Expected outcome:

    Looking up: java:comp/env/jdbc/example
    Found: com.sun.gjc.spi.jdbc40.DataSource40@49867870
    Driver name: PostgreSQL Native Driver

(Actual hashcode may vary. Results confirmed on Glassfish 3.1.2.2.)

### Actual outcome:

    Looking up: java:comp/env/jdbc/example
    Found: com.sun.gjc.spi.jdbc40.DataSource40@1ee50dc
    Failed.
    java.sql.SQLException: Error in allocating a connection. Cause: Connection could not be allocated because: java.net.ConnectException : Error connecting to server localhost on port 1527 with message Connection refused.
        at com.sun.gjc.spi.base.AbstractDataSource.getConnection(AbstractDataSource.java:121)
        at com.example.gf4.JdbcLookupServlet.doGet(JdbcLookupServlet.java:28)
        at javax.servlet.http.HttpServlet.service(HttpServlet.java:687)
        at javax.servlet.http.HttpServlet.service(HttpServlet.java:790)
        at org.apache.catalina.core.StandardWrapper.service(StandardWrapper.java:1682)
        at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:318)
        at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:160)
        at org.apache.catalina.core.StandardPipeline.doInvoke(StandardPipeline.java:734)
        at org.apache.catalina.core.StandardPipeline.invoke(StandardPipeline.java:673)
        at com.sun.enterprise.web.WebPipeline.invoke(WebPipeline.java:99)
        at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:174)
        at org.apache.catalina.connector.CoyoteAdapter.doService(CoyoteAdapter.java:357)
        at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:260)
        at com.sun.enterprise.v3.services.impl.ContainerMapper.service(ContainerMapper.java:188)
        at org.glassfish.grizzly.http.server.HttpHandler.runService(HttpHandler.java:191)
        at org.glassfish.grizzly.http.server.HttpHandler.doHandle(HttpHandler.java:168)
        at org.glassfish.grizzly.http.server.HttpServerFilter.handleRead(HttpServerFilter.java:189)
        at org.glassfish.grizzly.filterchain.ExecutorResolver$9.execute(ExecutorResolver.java:119)
        at org.glassfish.grizzly.filterchain.DefaultFilterChain.executeFilter(DefaultFilterChain.java:288)
        at org.glassfish.grizzly.filterchain.DefaultFilterChain.executeChainPart(DefaultFilterChain.java:206)
        at org.glassfish.grizzly.filterchain.DefaultFilterChain.execute(DefaultFilterChain.java:136)
        at org.glassfish.grizzly.filterchain.DefaultFilterChain.process(DefaultFilterChain.java:114)
        at org.glassfish.grizzly.ProcessorExecutor.execute(ProcessorExecutor.java:77)
        at org.glassfish.grizzly.nio.transport.TCPNIOTransport.fireIOEvent(TCPNIOTransport.java:838)
        at org.glassfish.grizzly.strategies.AbstractIOStrategy.fireIOEvent(AbstractIOStrategy.java:113)
        at org.glassfish.grizzly.strategies.WorkerThreadIOStrategy.run0(WorkerThreadIOStrategy.java:115)
        at org.glassfish.grizzly.strategies.WorkerThreadIOStrategy.access$100(WorkerThreadIOStrategy.java:55)
        at org.glassfish.grizzly.strategies.WorkerThreadIOStrategy$WorkerThreadRunnable.run(WorkerThreadIOStrategy.java:135)
        at org.glassfish.grizzly.threadpool.AbstractThreadPool$Worker.doWork(AbstractThreadPool.java:564)
        at org.glassfish.grizzly.threadpool.AbstractThreadPool$Worker.run(AbstractThreadPool.java:544)
        at java.lang.Thread.run(Thread.java:744)
    Caused by: javax.resource.spi.ResourceAllocationException: Error in allocating a connection. Cause: Connection could not be allocated because: java.net.ConnectException : Error connecting to server localhost on port 1527 with message Connection refused.
        at com.sun.enterprise.connectors.ConnectionManagerImpl.internalGetConnection(ConnectionManagerImpl.java:319)
        at com.sun.enterprise.connectors.ConnectionManagerImpl.allocateConnection(ConnectionManagerImpl.java:196)
        at com.sun.enterprise.connectors.ConnectionManagerImpl.allocateConnection(ConnectionManagerImpl.java:171)
        at com.sun.enterprise.connectors.ConnectionManagerImpl.allocateConnection(ConnectionManagerImpl.java:166)
        at com.sun.gjc.spi.base.AbstractDataSource.getConnection(AbstractDataSource.java:114)
        ... 30 more
    Caused by: com.sun.appserv.connectors.internal.api.PoolingException: Connection could not be allocated because: java.net.ConnectException : Error connecting to server localhost on port 1527 with message Connection refused.
        at com.sun.enterprise.resource.pool.datastructure.RWLockDataStructure.addResource(RWLockDataStructure.java:103)
        at com.sun.enterprise.resource.pool.ConnectionPool.addResource(ConnectionPool.java:282)
        at com.sun.enterprise.resource.pool.ConnectionPool.createResourceAndAddToPool(ConnectionPool.java:1512)
        at com.sun.enterprise.resource.pool.ConnectionPool.createResources(ConnectionPool.java:944)
        at com.sun.enterprise.resource.pool.ConnectionPool.initPool(ConnectionPool.java:230)
        at com.sun.enterprise.resource.pool.ConnectionPool.internalGetResource(ConnectionPool.java:511)
        at com.sun.enterprise.resource.pool.ConnectionPool.getResource(ConnectionPool.java:381)
        at com.sun.enterprise.resource.pool.PoolManagerImpl.getResourceFromPool(PoolManagerImpl.java:245)
        at com.sun.enterprise.resource.pool.PoolManagerImpl.getResource(PoolManagerImpl.java:170)
        at com.sun.enterprise.connectors.ConnectionManagerImpl.getResource(ConnectionManagerImpl.java:360)
        at com.sun.enterprise.connectors.ConnectionManagerImpl.internalGetConnection(ConnectionManagerImpl.java:307)
        ... 34 more
    Caused by: com.sun.appserv.connectors.internal.api.PoolingException: Connection could not be allocated because: java.net.ConnectException : Error connecting to server localhost on port 1527 with message Connection refused.
        at com.sun.enterprise.resource.pool.ConnectionPool.createSingleResource(ConnectionPool.java:924)
        at com.sun.enterprise.resource.pool.ConnectionPool.createResource(ConnectionPool.java:1189)
        at com.sun.enterprise.resource.pool.datastructure.RWLockDataStructure.addResource(RWLockDataStructure.java:98)
        ... 44 more
    Caused by: com.sun.appserv.connectors.internal.api.PoolingException: Connection could not be allocated because: java.net.ConnectException : Error connecting to server localhost on port 1527 with message Connection refused.
        at com.sun.enterprise.resource.allocator.LocalTxConnectorAllocator.createResource(LocalTxConnectorAllocator.java:110)
        at com.sun.enterprise.resource.pool.ConnectionPool.createSingleResource(ConnectionPool.java:907)
        ... 46 more
    Caused by: javax.resource.spi.ResourceAllocationException: Connection could not be allocated because: java.net.ConnectException : Error connecting to server localhost on port 1527 with message Connection refused.
        at com.sun.gjc.spi.DSManagedConnectionFactory.createManagedConnection(DSManagedConnectionFactory.java:129)
        at com.sun.enterprise.resource.allocator.LocalTxConnectorAllocator.createResource(LocalTxConnectorAllocator.java:87)
        ... 47 more
    Caused by: java.sql.SQLNonTransientConnectionException: java.net.ConnectException : Error connecting to server localhost on port 1527 with message Connection refused.
        at org.apache.derby.client.am.SQLExceptionFactory40.getSQLException(Unknown Source)
        at org.apache.derby.client.am.SqlException.getSQLException(Unknown Source)
        at org.apache.derby.jdbc.ClientDataSource.getConnection(Unknown Source)
        at com.sun.gjc.spi.DSManagedConnectionFactory.createManagedConnection(DSManagedConnectionFactory.java:115)
        ... 48 more
    Caused by: org.apache.derby.client.am.DisconnectException: java.net.ConnectException : Error connecting to server localhost on port 1527 with message Connection refused.
        at org.apache.derby.client.net.NetAgent.<init>(Unknown Source)
        at org.apache.derby.client.net.NetConnection.newAgent_(Unknown Source)
        at org.apache.derby.client.am.Connection.initConnection(Unknown Source)
        at org.apache.derby.client.am.Connection.<init>(Unknown Source)
        at org.apache.derby.client.net.NetConnection.<init>(Unknown Source)
        at org.apache.derby.client.net.NetConnection40.<init>(Unknown Source)
        at org.apache.derby.client.net.ClientJDBCObjectFactoryImpl40.newNetConnection(Unknown Source)
        at org.apache.derby.jdbc.ClientDataSource.getConnectionX(Unknown Source)
        ... 50 more
    Caused by: java.net.ConnectException: Connection refused
        at java.net.PlainSocketImpl.socketConnect(Native Method)
        at java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:339)
        at java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:200)
        at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:182)
        at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:392)
        at java.net.Socket.connect(Socket.java:579)
        at java.net.Socket.connect(Socket.java:528)
        at java.net.Socket.<init>(Socket.java:425)
        at java.net.Socket.<init>(Socket.java:208)
        at javax.net.DefaultSocketFactory.createSocket(SocketFactory.java:271)
        at org.apache.derby.client.net.OpenSocketAction.run(Unknown Source)
        at java.security.AccessController.doPrivileged(Native Method)
        ... 58 more

If you break into a debugger, you can discover (eventually) that the returned
pool is from the `jdbc/__default` binding; the `org.apache.derby` references
in the stack trace are an artifact of `DerbyPool`, to which `jdbc/__default`
points.

## Workaround

Explicitly binding the `jdbc/example` `resource-ref-name` using
`glassfish-web.xml` causes Glassfish to use the specified JNDI name. The
easiest way to do this is with the `lookup-name` subattribute in `web.xml`:

    <web-app xmlns="http://java.sun.com/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="
            http://java.sun.com/xml/ns/javaee
                http://www.oracle.com/webfolder/technetwork/jsc/xml/ns/javaee/web-app_3_0.xsd"
        version="3.0">
        <module-name>jndi-jdbc-bug</module-name>

        <resource-ref>
            <res-ref-name>jdbc/example</res-ref-name>
            <res-type>javax.sql.DataSource</res-type>
            <res-auth>Container</res-auth>
            <lookup-name>jdbc/example</lookup-name>
        </resource-ref>
    </web-app>

Alternately, you can specify it using Glassfish's own deployment descriptor,
and pass it along to `asadmin` at deploy time:

    cat > glassfish-web.xml <<'GLASSFISH_WEB_XML'
    <glassfish-web-app>
        <context-root>/</context-root>
        <resource-ref>
            <res-ref-name>jdbc/example</res-ref-name>
            <jndi-name>jdbc/example</jndi-name>
        </resource-ref>
    </glassfish-web-app>
    GLASSFISH_WEB_XML
    asadmin undeploy jndi-jdbc-bug
    asadmin deploy \
        --runtimealtdd glassfish-web.xml \
        target/jndi-jdbc-bug-1.0-SNAPSHOT.war

However, this is ineffective with `--upload` as `asadmin` does not upload the
passed alternate deployment descriptor.

You can also package the runtime deployment descriptor in a deployment plan:

    jar cf target/deployment-plan.jar glassfish-web.xml
    asadmin undeploy jndi-jdbc-bug
    asadmin deploy \
        --upload \
        --deploymentplan target/deployment-plan.jar \
        target/jndi-jdbc-bug-1.0-SNAPSHOT.war

This is compatible with `--upload`, but requires more packaging.