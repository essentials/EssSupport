<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>Essentials Bugtracker Lite</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="Essentials">

        <link href="//netdna.bootstrapcdn.com/bootswatch/2.1.0/united/bootstrap.min.css" rel="stylesheet">
        <style type="text/css">
            body {
                padding-top: 60px;
                padding-bottom: 40px;
            }
            .alert {
                display: none;
            }
            tr {
                cursor: pointer;
            }
        </style>
        <!-- Scripts -->
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.1/jquery.min.js"></script>
        <script src="//netdna.bootstrapcdn.com/twitter-bootstrap/2.1.1/js/bootstrap.min.js"></script>
    </head>

    <body>
        <!-- Title Bar -->
        <div class="navbar navbar-inverse navbar-fixed-top">
            <!-- Fork me on GitHub -->
            <a href="https://github.com/you">
                <img style="position: absolute; top: 0; right: 0; border: 0;" src="//s3.amazonaws.com/github/ribbons/forkme_right_red_aa0000.png" alt="Fork me on GitHub">
            </a>
            <!-- Navbar -->
            <div class="navbar-inner">
                <div class="container">
                    <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </a>
                    <!-- Branding -->
                    <a class="brand" href="/">Essentials Bug Tracker Lite</a>
                    <div class="nav-collapse collapse">
                        <ul class="nav">
                            <li><a href="/">Home</a></li>
                            <li><a href="create.jsp">Create</a></li>
                            <li><a href="view.jsp?state=open">Open</a></li>
                            <li><a href="view.jsp?state=closed">Closed</a></li>
                            <li><a href="view.jsp?state=mine">My</a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

        <!-- Main container -->
        <div class="container">
