import GitSubSystem.Commit;
import GitSubSystem.GitHandler;
import JiraSubSystem.JiraHandler;
import JiraSubSystem.Resolution;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * RepositoryMiner Extract Information from a git repository using information from Jira.
 */
public class RepositoryMiner {
    private GitHandler gitHandler;
    private JiraHandler jiraHandler;

    /**
     * Constructor of RepositoryMiner instance.
     * @param gitPath must be a File object that point to /project/.git directory
     * @param jiraProjectName Project Name ID on JIRA issue.apache
     * @throws IOException
     */
    public RepositoryMiner(File gitPath, String jiraProjectName) throws IOException {
        gitHandler = new GitHandler(gitPath);
        jiraHandler = new JiraHandler(jiraProjectName);
    }

    /**
     * constructor of RepositoryMiner.
     * @param url url pointing the the remote git repository
     * @param newRepoPath File object pointing to where to clone the remote repository.
     * @param jiraProjectName Project Name ID on Jira issue.apache
     * @throws IOException
     * @throws GitAPIException
     */
    public RepositoryMiner(String url, File newRepoPath, String jiraProjectName) throws IOException, GitAPIException {
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
        List<String> bugIDs = jiraHandler.getBugTicketID(Resolution.done);
        List<Commit> commits = new LinkedList<Commit>();
        for (String bugID : bugIDs) {
            for (Commit commit : gitHandler.grep("JIRA:\n( )*\\[" + bugID + "\\]")) {
                commits.add(commit);
            }
        }
        return commits;
    }

}
