package net.ess3.tracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EssentialsVersions extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader br = null;
        try {
            URLConnection con = new URL("http://ci.ess3.net/guestAuth/app/rest/buildTypes/id:bt3/builds").openConnection();
            con.setRequestProperty("Accept", "application/json");
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            resp.setContentType("application/json");
            while ((line = br.readLine()) != null) {
                resp.getWriter().write(line);
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }
}
