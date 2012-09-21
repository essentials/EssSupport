<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
<div class="hero-unit">
    <h2>Essentials Bug Tracker Lite</h2>
    <p>
        Welcome to Essentials, Bukkit's number one all purpose plugin! This site is an interface to our JIRA bug tracker found <a href="http://essentials3.atlassian.net/">here</a>
        and allows for the easy creation and viewing of issues.
        <br>
        You do not need an account to view or comment on issues here, so hop right in and give us some feedback!
    </p>
</div>

<div class="row">
    <div class="span3">
        <h3>Create an Issue</h3>
        <p>Found a bug in Essentials? We would love to know about it!</p>
        <p><a class="btn" href="create.jsp">Create an issue &raquo;</a></p>
    </div>
    <div class="span3">
        <h3>View open issues</h3>
        <p>Want to see current issues? This is where they will be.</p>
        <p><a class="btn" href="view.jsp?state=open">View open issues &raquo;</a></p>
    </div>
    <div class="span3">
        <h3>View closed issues</h3>
        <p>Want to check closed issues? Here they are.</p>
        <p><a class="btn" href="view.jsp?state=closed">View closed issues &raquo;</a></p>
    </div>
    <div class="span3">
        <h3>View my issues</h3>
        <p>Previously submitted a request to us? Track its status here.</p>
        <p><a class="btn" href="view.jsp?state=mine">View my issues &raquo;</a></p>
    </div>
</div>
<%@include file="footer.jsp" %>
