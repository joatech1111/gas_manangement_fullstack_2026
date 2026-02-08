package com.joainfo.common.util;

import org.apache.log4j.Logger;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ServerPortLoggingListener implements ServletContextListener {
    private static final Logger logger = Logger.getLogger(ServerPortLoggingListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            Set<ObjectName> names = mbs.queryNames(new ObjectName("Catalina:type=Connector,*"), null);
            if (names == null || names.isEmpty()) {
                logger.info("Server port check: no Catalina connectors found.");
                return;
            }

            List<String> connectors = new ArrayList<String>();
            for (ObjectName name : names) {
                Object port = mbs.getAttribute(name, "port");
                Object protocol = mbs.getAttribute(name, "protocol");
                connectors.add(String.valueOf(protocol) + ":" + String.valueOf(port));
            }

            StringBuilder sb = new StringBuilder();
            sb.append("\n");
            sb.append("========================================\n");
            sb.append("         SERVER PORTS DETECTED          \n");
            sb.append("========================================\n");
            for (String c : connectors) {
                sb.append("  -> ").append(c).append("\n");
            }
            sb.append("========================================");
            logger.info(sb.toString());
        } catch (Exception e) {
            logger.warn("Server port check failed.", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // no-op
    }
}
