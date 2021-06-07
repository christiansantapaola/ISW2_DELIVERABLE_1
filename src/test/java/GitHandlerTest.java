import GitSubSystem.Commit;
import GitSubSystem.GitHandler;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.util.List;


public class GitHandlerTest {

    static void printCommit(RevCommit commit) {
        System.out.println("commit: " + commit);
        System.out.println("    author: " + commit.getAuthorIdent());
        System.out.println("    commit Time: " + commit.getCommitTime());
        System.out.println("    commit Ident: " + commit.getCommitterIdent());
        System.out.println("    Full Message: " + commit.getFullMessage());
    }

    @org.junit.jupiter.api.Test
    void CloneRepository() throws Exception {
        Git git = Git.cloneRepository()
                .setURI("https://github.com/apache/incubator-s2graph")
                .setDirectory(new File("repository"))
                .setCloneAllBranches(true)
                .call();
    }

    @org.junit.jupiter.api.Test
    void openRepository() throws Exception {
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        repositoryBuilder.setMustExist(true);
        repositoryBuilder.setGitDir(new File("repository/.git/"));
        Repository repository = repositoryBuilder.build();
        Git git = new Git(repository);
    }

    @org.junit.jupiter.api.Test
    void Log() throws Exception {
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        repositoryBuilder.setMustExist(true);
        repositoryBuilder.setGitDir(new File("repository/.git/"));
        Repository repository = repositoryBuilder.build();
        Git git = new Git(repository);
        ObjectId head = repository.resolve(Constants.HEAD);
        Iterable<RevCommit> commits = git.log().add(head).call();
        for (RevCommit commit : commits) {
            System.out.println("commit: " + commit);
            System.out.println("    author: " + commit.getAuthorIdent());
            System.out.println("    commit Time: " + commit.getCommitTime());
            System.out.println("    commit Ident: " + commit.getCommitterIdent());
            System.out.println("    Full Message: " + commit.getFullMessage());
        }
    }

    @org.junit.jupiter.api.Test
    void gitGrepTest() throws Exception {
        GitHandler gitHandler = new GitHandler(new File("repository/S2GRAPH/.git/"));
        List<Commit> commits = gitHandler.grep("\\[S2GRAPH-206\\]");
        for (Commit commit : commits) {
           System.out.println(commit);
        }
    }
}
