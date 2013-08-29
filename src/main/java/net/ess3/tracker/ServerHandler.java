package net.ess3.tracker;

import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.exceptions.JadeCompilerException;
import de.neuland.jade4j.template.FileTemplateLoader;
import de.neuland.jade4j.template.JadeTemplate;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class ServerHandler extends AbstractHandler {

    private final JadeConfiguration jade;
    private final VersionHandler versions;

    public ServerHandler() {
        jade = new JadeConfiguration();
        jade.setCaching(false);
        jade.setTemplateLoader(new FileTemplateLoader("src/main/resources/", "UTF-8"));
        jade.setPrettyPrint(true);

        versions = new VersionHandler();
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String templateName = target.equals("/") ? "root" : target;

        JadeTemplate template = jade.getTemplate(templateName);

        Map<String, Object> data = new HashMap<>();
        data.put("essVersions", versions.getEssentialsVersions());
        data.put("serverVersions", versions.getServerVersions());

        try {
            String page = jade.renderTemplate(template, data);
            response.getWriter().write(page);
        } catch (JadeCompilerException ex) {
            ex.printStackTrace();
        }


        baseRequest.setHandled(true);
    }
}
