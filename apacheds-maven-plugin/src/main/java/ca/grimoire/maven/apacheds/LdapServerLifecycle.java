package ca.grimoire.maven.apacheds;

import java.io.File;
import java.util.List;

import org.apache.directory.server.configuration.ApacheDS;
import org.apache.directory.server.constants.ServerDNConstants;
import org.apache.directory.server.core.DefaultDirectoryService;
import org.apache.directory.server.core.factory.JdbmPartitionFactory;
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmPartition;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.server.protocol.shared.transport.TcpTransport;
import org.apache.directory.shared.ldap.constants.SchemaConstants;
import org.apache.directory.shared.ldap.schema.ldif.extractor.SchemaLdifExtractor;
import org.apache.directory.shared.ldap.schema.ldif.extractor.impl.DefaultSchemaLdifExtractor;

/**
 * A component for hosting an LDAP server.
 */
public class LdapServerLifecycle {

    private DefaultDirectoryService service;
    private LdapServer ldapService;
    private ApacheDS apacheDs;

    public void startServer() throws Exception {
        apacheDs.startup();
    }

    public void configureLdapServer(int port) throws Exception {
        ldapService = new LdapServer();
        ldapService.setTransports(new TcpTransport(port));
        ldapService.setDirectoryService(service);

        apacheDs = new ApacheDS(ldapService);
    }

    public void stopServer() throws Exception {
        apacheDs.shutdown();
    }

    public void reset(File workingDirectory) throws Exception {
        SchemaLdifExtractor extractor = new DefaultSchemaLdifExtractor(
                workingDirectory);
        extractor.extractOrCopy(true);

        service = new DefaultDirectoryService();
        service.setWorkingDirectory(workingDirectory);
        service.getChangeLog().setEnabled(false);
        service.setSystemPartition(createSystemPartition(service,
                workingDirectory));
    }

    public void addPartitions(List<Partition> partitions) throws Exception {
        for (Partition partition : partitions)
            service.addPartition(partition.createJdbmPartition());
    }

    public void configureLdifDirectory(File ldifDirectory) {
        apacheDs.setLdifDirectory(ldifDirectory);
    }

    private JdbmPartition createSystemPartition(DefaultDirectoryService service,
            File workingDirectory) throws Exception {
        JdbmPartitionFactory partitionFactory = new JdbmPartitionFactory();
        JdbmPartition systemPartition = partitionFactory
                .createPartition("system",
                        ServerDNConstants.SYSTEM_DN,
                        500,
                        new File(workingDirectory, "system"));
        partitionFactory.addIndex(systemPartition,
                SchemaConstants.OBJECT_CLASS_AT,
                100);

        systemPartition.setSchemaManager(service.getSchemaManager());
        return systemPartition;
    }
}
