<%@page import="java.util.List"%>
<%@page import="com.atlassian.jira.rest.client.domain.Issue"%>
<%@page import="net.ess3.tracker.Main"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String p = request.getParameter("page");
    if (p == null) {
        p = "1";
    }
    int pageNum = Integer.parseInt(p);
    String state = request.getParameter("state");
    boolean hasNext = pageNum - 1 > 0;
%>
<%@include file="header.jsp" %>

<table class="table table-hover table-bordered">
    <thead>
        <tr>
            <th>Type</th>
            <th>Key</th>
            <th>Summary</th>
            <th>Assignee</th>
            <th>Reporter</th>
            <th>Priority</th>
            <th>Status</th>
            <th>Resolution</th>
            <th>Created</th>
            <th>Updated</th>
        </tr>
    </thead>
    <tbody>
        <% for (Issue i : Main.getIssues(state, pageNum, (List) request.getSession().getAttribute("issues"))) {%>
        <tr data-url="<%="https://essentials3.atlassian.net/browse/" + i.getKey()%>">
            <td><%= i.getIssueType().getName()%></td>
            <td><%= i.getKey()%></td>
            <td><%= i.getSummary()%></td>
            <td><%= i.getAssignee() == null ? "Unassigned" : i.getAssignee().getDisplayName()%></td>
            <td><%= i.getReporter() == null ? "Anonymous" : i.getReporter().getDisplayName()%></td>
            <td><%= i.getPriority().getName()%></td>
            <td><%= i.getStatus().getName()%></td>
            <td><%= i.getResolution().getName()%></td>
            <td><%= i.getCreationDate().toString("dd/MM/YY")%></td>
            <td><%= i.getUpdateDate().toString("dd/MM/YY")%></td>
        </tr>
        <% }%>
    </tbody>
</table>

<div class="pagination">
    <ul>
        <li <%=(!hasNext) ? " class='disabled'" : ""%>><a <%=(hasNext) ? "href='?state=" + state + "&page=" + (pageNum - 1) + "'" : ""%>>Prev</a></li>
        <li><a <%="href='?state=" + state + "&page=" + (pageNum + 1) + "'"%>>Next</a></li>
    </ul>
</div>

<script type="text/javascript">
    $("tr").click(function () {
        window.open($(this).attr("data-url"));
    });
</script>
<%@include file="footer.jsp" %>
