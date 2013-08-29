package net.ess3.tracker;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.auth.AnonymousAuthenticationHandler;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.input.IssueInput;
import com.atlassian.jira.rest.client.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;

public class Main extends HttpServlet {

    private static final String url = "https://essentials3.atlassian.net/";
    private static final JiraRestClient client;
    private static final Comparator<Issue> comp = new Comparator<Issue>() {
        @Override
        public int compare(Issue o1, Issue o2) {
            return o2.getKey().compareTo(o1.getKey());
        }
    };

    static {
        try {
            client = new AsynchronousJiraRestClientFactory().create(new URI(url), new AnonymousAuthenticationHandler());
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Collection<Issue> getIssues(String state, int page, List<BasicIssue> myIssues) throws Exception {
        // init return array
        final Set<Issue> ret = new TreeSet<>(comp);
        // make a query
        String query = state == null ? "" : "status = " + state;
        // do the query
        Iterable<BasicIssue> issues;
        // if they are reserved issues
        if ("mine".equals(state)) {
            issues = myIssues;
        } else {
            // search for them
            issues = client.getSearchClient().searchJql(query, 20, page * 20 - 20).get().getIssues();
        }
        // null check
        if (issues != null) {
            // loop through them
            List<Promise<Issue>> promises = new ArrayList<>();
            for (BasicIssue basic : issues) {
                promises.add(client.getIssueClient().getIssue(basic.getKey()));
            }
            for (Promise<Issue> p : promises) {
                ret.add(p.claim());
            }
        }

        // return
        return ret;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // add stuff
        String summary = req.getParameter("summary");
        String cbVersion = req.getParameter("cb-version");
        String essVersion = req.getParameter("ess-version");
        String description = req.getParameter("description");
        // construct the issue
        IssueInput issue = new IssueInputBuilder("ESS", 1L)
                .setSummary(summary)
                .setDescription(description)
                .setFieldValue("environment", "Essentials: " + essVersion + " CraftBukkit: " + cbVersion)
                .build();
        // submit it
        BasicIssue created = client.getIssueClient().createIssue(issue).claim();
        // plain text
        resp.setContentType("text/plain");
        // write url
        resp.getWriter().write("https://essentials3.atlassian.net/browse/");
        // write issue
        resp.getWriter().write(created.getKey());
        // set cookie
        List<BasicIssue> myIssues = (List<BasicIssue>) req.getSession().getAttribute("issues");
        if (myIssues == null) {
            myIssues = new ArrayList<>();
        }
        myIssues.add(created);
        req.getSession().setAttribute("issues", myIssues);
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(8888);
        server.setHandler(new ServerHandler());
        server.start();
    }
}
