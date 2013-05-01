<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
<!-- Forms plugin -->
<script src="http://malsup.github.com/jquery.form.js"></script>
<div class="row span12">
    <div class="well">
        <h1>Create an issue</h1>
        <p>Use this page to submit an issue so we can look into it.<br>Please include a short summary and then a full description telling us everything we need to know.</p>
    </div>
    <hr>
    <div class="alert">cats</div>
    <form class="form-horizontal" action="/issues/submit" method="POST" id="create">
        <div class="control-group">
            <label class="control-label">Summary</label>
            <div class="controls">
                <input class="input-xxlarge" type="text" placeholder="Brief summary of your issue" id="summary" name="summary">
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">CraftBukkit version</label>
            <div class="controls">
                <select id="craftbukkit-versions" name="cb-version">
                    <option>Loading CraftBukkit versions</option>
                </select>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">Essentials version</label>
            <div class="controls">
                <select id="plugin-versions" name="ess-version">
                    <option>Loading Essentials versions</option>
                </select>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">Description</label>
            <div class="controls">
                <textarea name="description" id="description" class="input-xxlarge" rows="5" placeholder="Please provide an in depth description on how to reproduce this issue / why you want this feature."></textarea>
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <button type="submit" class="btn">Create Issue</button>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript">
    // enable buttons
    $("button").attr("disabled", false);
    // Populate CB builds
    $.getJSON("http://dl.bukkit.org/api/1.0/downloads/projects/craftbukkit/artifacts/beta/?_accept=application/json-p&callback=?", function(data) {
        var tag = $("#craftbukkit-versions");
        tag.html("");
        $.each(data.results, function(index, val) {
            var build = val.version;
            tag.append($("<option>", { value : build, text: build}));
        });
    });
    // Essentials builds
    $.getJSON("/versions/essentials", function(data){
        var tag = $("#plugin-versions");
        tag.html("");
        $.each(data.build, function(index, val) {
            var build = val.number;
            tag.append($("<option>", { value : build, text: build}));
        });
    });
    // Handle form submission
    $("form").submit(function(e) {
        e.preventDefault();
        // check for validation
        // summary
        if ($("#summary").val() == ""){
            setAlert("Please enter a summary of your issue", "alert-error");
            return;
        }
        // description
        if ($("#description").val() == ""){
            setAlert("Please provide a brief description of your issue", "alert-error");
            return;
        }
        // submit the form
        $("button").attr("disabled", true);
        setAlert("Submitting your issue. Please be patient, the page will automatically refresh.");
        $(this).ajaxSubmit(function(r) {
            window.location = r;
        });
    });
    var alertBox = $(".alert");
    function setAlert(text, clazz) {
        alertBox.removeClass();
        alertBox.addClass("alert");
        if (clazz != null){
            alertBox.addClass(clazz);
        }
        alertBox.html(text);
        alertBox.slideDown();
    }
</script>
<%@include file="footer.jsp" %>
