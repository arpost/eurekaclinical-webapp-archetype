package ${package}.webapp.config;


import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;
import org.eurekaclinical.common.config.InjectorSupport;
import ${package}.webapp.props.WebappProperties;
import ${package}.client.Client;

import javax.servlet.ServletContextEvent;
import java.util.ResourceBundle;

/**
 * Created by akalsan on 9/20/16.
 *  Loaded up on application initialization, sets up the application with Guice
 * injector and any other bootup processes.
 */
public class ContextListener extends GuiceServletContextListener {

    private final WebappProperties properties = new WebappProperties();
    private Client client = new Client(this.properties.getServiceUrl());
    private Injector injector;

    @Override
    protected Injector getInjector() {
        this.injector = new InjectorSupport(
                new Module[]{
                    new AppModule(this.properties, this.client),
                    new ServletModule(this.properties),},
                this.properties).getInjector();
        return this.injector;
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);
        servletContextEvent.getServletContext().addListener(this.injector.getInstance(ClientSessionListener.class));
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        super.contextDestroyed(servletContextEvent);
        this.client.close();
    }
}


