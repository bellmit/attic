package ca.grimoire.maven.apacheds;

import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmPartition;
import org.apache.directory.shared.ldap.exception.LdapInvalidDnException;

public class Partition {
    private String id;
    private String baseDn;

    public JdbmPartition createJdbmPartition() throws LdapInvalidDnException {
        JdbmPartition partition = new JdbmPartition();

        partition.setId(id);
        partition.setSuffix(baseDn);

        return partition;
    }

    @Override
    public String toString() {
        return String.format("Partition (id='%s', baseDn='%s')", id, baseDn);
    }
}
