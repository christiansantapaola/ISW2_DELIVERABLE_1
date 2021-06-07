package GitSubSystem;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.filter.MessageRevFilter;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * GitSubSystem.GitHandler is a class which handle all the git subsystem.
 * All Git related operation should be contained in this class.
 */
public class GitHandler {
    private Repository repository;

    /**
     * Constructor of GitSubSystem.GitHandler instance from local git repository.
     * @param localRepositoryGit, File pointing to existing Project/.git folder
     * @throws IOException
     */
    public GitHandler(File localRepositoryGit) throws IOException {
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        repositoryBuilder.setMustExist(true);
        repositoryBuilder.setGitDir(localRepositoryGit);
        repository = repositoryBuilder.build();
    }

    /**
     * Constructor of GitSubSystem.GitHandler instance from a remote git repository.
     * @param url, url of the remote repository.
     * @param newLocalRepository, File object pointing to where to clone the remote repository.
     * @throws IOException
     * @throws GitAPIException
     */
    public GitHandler(String url, File newLocalRepository) throws IOException, GitAPIException{
        Git git = Git.cloneRepository()
                .setURI(url)
                .setDirectory(newLocalRepository)
                .setCloneAllBranches(true)
                .call();
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        repositoryBuilder.setMustExist(true);
        repositoryBuilder.setGitDir(newLocalRepository);
        repository = repositoryBuilder.build();
    }


    /**
     * This method emulate git-log --grep=pattern
     * @param pattern a String object containing a pattern.
     * @return an Iterable<RevCommit>, the RevCommit contain the information on the commit requested.
     * @throws IOException
     * @throws GitAPIException
     * @throws NoHeadException
     * @throws MissingObjectException
     * @throws IncorrectObjectTypeException
     */
    public List<Commit> grep(String pattern)
            throws IOException, GitAPIException, NoHeadException, MissingObjectException, IncorrectObjectTypeException {
        Git git = new Git(repository);
        ObjectId head = repository.resolve(Constants.HEAD);
        RevFilter revFilter = MessageRevFilter.create(pattern);
        Iterable<RevCommit> commits = git.log().add(head).setRevFilter(revFilter).call();
        List<Commit> result = new ArrayList<Commit>();
        for (RevCommit commit : commits) {
            Commit tmp = new Commit(commit);
            result.add(tmp);
        }
        return result;
    }


}
