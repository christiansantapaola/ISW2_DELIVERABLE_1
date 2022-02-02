package it.uniroma2.dicii.santapaola.christian.jira;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

/**
 *  it.uniroma2.dicii.santapaola.christian.JiraSubSystem.JiraHandler is a class which interface with JIRA.
 *  This class will handle query to jira given a project, parse the output and return it in a
 *  format usable in java.
 *
 * @author Christian Santapaola
 */
public class JiraHandler {


    private String projectName;


    /**
     * Constructor of it.uniroma2.dicii.santapaola.christian.JiraSubSystem.JiraHandler
     * @param projectName, is the name of the project on JIRA.
     */
    public JiraHandler(String projectName) {
        this.projectName = projectName;
    }


    /**
     * getter of projectName
     * @return String ProjectName
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * getBugTicketID() send a query to JIRA and return a list of bug identifier in the format [projectName-ID]
     * @param resolution: needed to select the resolution parameters inside the query,
     *                  some project may use 'fixed' or 'done' to mark fixed bug.
     * @return List<String> containg the bug identifier in the format [projectName-ID]
     * @throws IOException
     * @throws JSONException
     */
    public List<String> getBugTicketID(Resolution resolution) throws IOException, JSONException {

        Integer j = 0;
        Integer i = 0;
        Integer total = 1;
        List<String> result = new LinkedList<>();
        //Get JSON API for closed bugs w/ AV in the project
        do {
            //Only gets a max of 1000 at a time, so must do this multiple times if bugs >1000
            j = i + 1000;
            String url = queryBuilder(QueryStatus.STATUS_ALL, resolution, i.toString(), j.toString());
            JSONObject json = readJsonFromUrl(url);
            JSONArray issues = json.getJSONArray("issues");
            total = json.getInt("total");
            for (; i < total && i < j; i++) {
                //Iterate through each bug
                String key = issues.getJSONObject(i%1000).get("key").toString();
                result.add(key);
            }
        } while (i < total);
        return result;
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }


    private JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try (BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));) {
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        } finally {
            is.close();
        }
    }

    /**
     * build a query, may be changed to select a query.
     * @param queryStatus type of query.
     * @param resolution type of resolution.
     * @param startAt starting id.
     * @param maxResult how many identifier select at max after startAt
     * @return String query, the finite query to issues.apache
     */
    private String queryBuilder(QueryStatus queryStatus, Resolution resolution, String startAt, String maxResult) {
        String result = "";
        switch (queryStatus) {
            case STATUS_CLOSED_OR_RESOLVED:
                result = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
                    + projectName + "%22AND%22issueType%22=%22Bug%22AND(%22status%22=%22closed%22OR"
                    + "%22status%22=%22resolved%22)AND%22resolution%22=%22"
                    + resolution.toString() + "%22&fields=key,resolutiondate,versions,created&startAt="
                    + startAt + "&maxResults=" + maxResult;
                return result;
            case STATUS_ALL:
                result = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
                        + projectName + "%22AND%22issueType%22=%22Bug%22AND%22resolution%22=%22"
                        + resolution.toString() + "%22&fields=key,resolutiondate,versions,created&startAt="
                        + startAt + "&maxResults=" + maxResult;
                return result;
            default:
                return result;

        }
    }
}
