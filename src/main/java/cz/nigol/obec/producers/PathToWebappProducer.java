package cz.nigol.obec.producers;

import java.net.URISyntaxException;
import java.nio.file.Paths;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.nigol.obec.qualifiers.PathToWebapp;

@Named
@ApplicationScoped
public class PathToWebappProducer {
    @Inject
    private Log log;

    @Produces
    @PathToWebapp
    public String pathToWebapp() {
        String result = null;
        try {
            result = Paths.get(getClass().getClassLoader()
                    .getResource("findMe.txt").toURI()).toString();
            result = result.split("WEB-INF/classes/findMe.txt")[0];
        } catch (URISyntaxException e) {
            log.error(e);
        }
        return result;
    }
}