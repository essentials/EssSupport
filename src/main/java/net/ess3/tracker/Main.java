package net.ess3.tracker;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.auth.AnonymousAuthenticationHandler;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.BasicProject;
import com.atlassian.jira.rest.client.domain.BasicUser;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.IssueType;
import com.atlassian.jira.rest.client.domain.Priority;
import com.atlassian.jira.rest.client.domain.Project;
import com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    //
    private static Map<BasicIssue, Issue> issueCache = new HashMap<BasicIssue, Issue>();

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

    public static List<Issue> getIssues(String state, int page, List<BasicIssue> myIssues) {
        // init return array
        List<Issue> ret = new ArrayList<Issue>();
        // make a query
        String query = state == null ? "" : "status = " + state;
        // do the query
        Iterable<BasicIssue> issues;
        // if they are reserved issues
        if ("mine".equals(state)) {
            issues = myIssues;
        } else {
            // search for them
            issues = client.getSearchClient().searchJql(query, 20, page * 20 - 20, null).getIssues();
        }
        // null check
        if (issues != null) {
            // loop through them
            for (BasicIssue basic : issues) {
                // add it to the list
                ret.add(client.getIssueClient().getIssue(basic.getKey(), null));
            }
        }
        // return
        return ret;
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
