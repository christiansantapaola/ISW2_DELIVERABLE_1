package it.uniroma2.dicii.santapaola.christian;

import it.uniroma2.dicii.santapaola.christian.git.Commit;
import it.uniroma2.dicii.santapaola.christian.git.GitHandler;
import it.uniroma2.dicii.santapaola.christian.jira.JiraHandler;
import it.uniroma2.dicii.santapaola.christian.jira.Resolution;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * it.uniroma2.dicii.santapaola.christian.RepositoryMiner Extract Information from a git repository using information from Jira.
 */
public class RepositoryMiner {
    private GitHandler gitHandler;
    private JiraHandler jiraHandler;

    /**
     * Constructor of it.uniroma2.dicii.santapaola.christian.RepositoryMiner instance.
     * @param gitPath must be a File object that point to /project/.git directory
     * @param jiraProjectName Project Name ID on JIRA issue.apache
     * @throws IOException
     */
    public RepositoryMiner(File gitPath, String jiraProjectName) throws IOException {
        gitHandler = new GitHandler(gitPath);
        jiraHandler = new JiraHandler(jiraProjectName);
    }

    /**
     * constructor of it.uniroma2.dicii.santapaola.christian.RepositoryMiner.
     * @param url url pointing the the remote git repository
     * @param newRepoPath File object pointing to where to clone the remote repository.
     * @param jiraProjectName Project Name ID on Jira issue.apache
     * @throws IOException
     * @throws GitAPIException
     */
    public RepositoryMiner(String url, File newRepoPath, String jiraProjectName) throws GitAPIException {
        gitHandler = new GitHandler(url, newRepoPath);
        jiraHandler = new JiraHandler(jiraProjectName);
    }

    /**
     * getBugCommit extract all the commit of the fixed bug using the JIRA issues ID.
     * @return List<RevCommit> list of all commit.
     * @throws IOException
     * @throws GitAPIException
     */
    public List<Commit> getBugCommit() throws IOException, GitAPIException {
        List<String> bugIDs = jiraHandler.getBugTicketID(Resolution.DONE);
        List<Commit> commits = new LinkedList<>();
        for (String bugID : bugIDs) {
            for (Commit commit : gitHandler.grep("JIRA:\n( )*\\[" + bugID + "\\]")) {
                commits.add(commit);
            }
        }
        return commits;
    }

    public void close() {
        gitHandler.close();
    }
}
