import GitSubSystem.Commit;
import JiraSubSystem.JiraHandler;
import JiraSubSystem.Resolution;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

class RepositoryMinerTest {

    @Test
    void getBugCommit() throws IOException, GitAPIException {
        JiraHandler jiraHandler = new JiraHandler("S2GRAPH");
        RepositoryMiner miner = new RepositoryMiner(new File("repository/.git/"), "S2GRAPH");
        List<Commit> commits = miner.getBugCommit();
        List<String> id = jiraHandler.getBugTicketID(Resolution.done);
        for (Commit commit : commits) {
            System.out.println(commit);
        }

    }
}