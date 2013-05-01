package net.ess3.tracker;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.auth.AnonymousAuthenticationHandler;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.IssueFieldId;
import com.atlassian.jira.rest.client.domain.input.ComplexIssueInputFieldValue;
import com.atlassian.jira.rest.client.domain.input.FieldInput;
import com.atlassian.jira.rest.client.domain.input.IssueInput;
import com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClient;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class Main extends HttpServlet {

    private static final String url = "https://essentials3.atlassian.net/";
    private static final JiraRestClient client;
    private static final ComplexIssueInputFieldValue project = new ComplexIssueInputFieldValue((Map) Collections.singletonMap("key", "ESS"));
    private static final ComplexIssueInputFieldValue type = new ComplexIssueInputFieldValue((Map) Collections.singletonMap("name", "Story"));
    private static final ExecutorService pool = Executors.newCachedThreadPool();
    private static final Comparator<Issue> comp = new Comparator<Issue>() {
        @Override
        public int compare(Issue o1, Issue o2) {
            return o2.getKey().compareTo(o1.getKey());
        }
    };
    private static final Gson gson = new Gson();

    static {
        try {
            client = new JerseyJiraRestClient(new URI(url), new AnonymousAuthenticationHandler());
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Collection<Issue> getIssues(String state, int page, List<BasicIssue> myIssues) {
        // init return array
        final Set<Issue> ret = new TreeSet<>(comp);
        // make a query
        String query = state == null ? "" : "status = " + state;
        // do the query
        List<BasicIssue> issues;
        // if they are reserved issues
        if ("mine".equals(state)) {
            issues = myIssues;
        } else {
            // search for them
            issues = (List) client.getSearchClient().searchJql(query, 20, page * 20 - 20, null).getIssues();
        }
        // null check
        if (issues != null && !issues.isEmpty()) {
            // store count
            final int count = issues.size();
            final CountDownLatch done = new CountDownLatch(count);
            // loop through them
            for (final BasicIssue basic : issues) {
                // start a new service
                pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ret.add(client.getIssueClient().getIssue(basic.getKey(), null));
                            done.countDown();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }

            try {
                done.await();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
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
        FieldInput projectF = new FieldInput(IssueFieldId.PROJECT_FIELD, project);
        FieldInput typeF = new FieldInput(IssueFieldId.ISSUE_TYPE_FIELD, type);
        FieldInput summaryF = new FieldInput(IssueFieldId.SUMMARY_FIELD, summary);
        FieldInput descriptionF = new FieldInput(IssueFieldId.DESCRIPTION_FIELD, description);
        FieldInput environmentF = new FieldInput("environment", "Essentials: " + essVersion + " CraftBukkit: " + cbVersion);
        IssueInput issue = IssueInput.createWithFields(projectF, typeF, summaryF, descriptionF, environmentF);
        // submit it
        BasicIssue created = client.getIssueClient().createIssue(issue, null);
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
        WebAppContext context = new WebAppContext();
        context.setResourceBase("src/main/webapp");
        server.setHandler(context);
        server.start();
    }
}
