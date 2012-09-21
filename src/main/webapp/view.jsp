<%@page import="com.atlassian.jira.rest.client.domain.Issue"%>
<%@page import="net.ess3.tracker.Main"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <% for (Issue i : Main.getIssues("", 1)) {%>
        <tr>
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

<%@include file="footer.jsp" %>
