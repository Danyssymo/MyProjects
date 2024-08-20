package uno.dos.tres.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uno.dos.tres.dao.сonnectionpool.ConnectionPool;
import uno.dos.tres.dao.сonnectionpool.ConnectionPoolException;

public class ConnectionPoolLifeCircleContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ConnectionPool.getInstance();
        if (ConnectionPool.connectionPoolError) {
            sce.getServletContext().setAttribute("connectionPoolError", "Error");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            ConnectionPool.getInstance().dispose();
        } catch (ConnectionPoolException e) {
            //log
        }

    }

}
