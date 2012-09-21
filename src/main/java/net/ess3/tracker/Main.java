package net.ess3.tracker;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.auth.AnonymousAuthenticationHandler;
import com.atlassian.jira.rest.client.domain.BasicProject;
import com.atlassian.jira.rest.client.domain.IssueType;
import com.atlassian.jira.rest.client.domain.Priority;
import com.atlassian.jira.rest.client.domain.Project;
import com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class Main extends HttpServlet {

    public static List<IssueType> issueTypes;
    public static List<BasicProject> projects;
    public static List<Priority> priorities;
    //
    private static final Project project;
    private static final String url = "https://essentials3.atlassian.net/";
    private static final JiraRestClient client;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    static {
        try {
            client = new JerseyJiraRestClient(new URI(url), new AnonymousAuthenticationHandler());
            project = client.getProjectClient().getProject("ESS", null);
            issueTypes = (List) project.getIssueTypes();
            priorities = (List) client.getMetadataClient().getPriorities(null);
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(8888);
        WebAppContext context = new WebAppContext();
        context.setResourceBase("src/main/webapp");
        server.setHandler(context);
        server.start();
    }
}
