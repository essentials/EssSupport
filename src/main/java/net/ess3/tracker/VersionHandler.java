package net.ess3.tracker;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import lombok.Getter;

public class VersionHandler extends TimerTask {

    private final Timer timer;
    private final Gson gson;
    //
    @Getter
    private List<String> serverVersions = Collections.emptyList();
    @Getter
    private List<String> essentialsVersions = Collections.emptyList();

    public VersionHandler() {
        timer = new Timer("Version Scraper");
        gson = new Gson();

        timer.schedule(this, 0, TimeUnit.MINUTES.toMillis(5));
    }

    @Override
    public void run() {
        try {
            serverVersions = craftBukkit();
            Collections.sort(serverVersions);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            essentialsVersions = essentials();
            Collections.sort(essentialsVersions);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<String> essentials() throws IOException {
        List<String> ret = new ArrayList<>();

        JsonObject obj = get("http://ci.ess3.net/guestAuth/app/rest/buildTypes/id:bt3/builds");
        JsonArray builds = obj.getAsJsonArray("build");
        for (JsonElement el : builds) {
            ret.add(el.getAsJsonObject().get("number").getAsString());
        }

        return ret;
    }

    private List<String> craftBukkit() throws IOException {
        List<String> ret = new ArrayList<>();

        JsonObject obj = get("http://dl.bukkit.org/api/1.0/downloads/projects/craftbukkit/artifacts/beta/");
        JsonArray results = obj.getAsJsonArray("results");
        for (JsonElement el : results) {
            ret.add("CraftBukkit: " + el.getAsJsonObject().get("version").getAsString());
        }

        return ret;
    }

    private JsonObject get(String url) throws IOException {
        URLConnection con = new URL(url).openConnection();
        con.addRequestProperty("Accept", "application/json");
        con.addRequestProperty("User-Agent", "Essentials Support");

        try (InputStreamReader rd = new InputStreamReader(con.getInputStream())) {
            return gson.fromJson(rd, JsonObject.class);
        }
    }
}
