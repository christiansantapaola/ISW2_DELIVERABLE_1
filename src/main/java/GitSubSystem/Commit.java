package GitSubSystem;

import org.eclipse.jgit.revwalk.RevCommit;

import java.time.Instant;
import java.time.LocalDateTime;

public class Commit {
    String name;
    String author;
    String committer;
    String fullMessage;
    LocalDateTime committerCommitTime;
    LocalDateTime authorCommitTime;

    public Commit(RevCommit commit) {
        name = commit.getName();
        author = commit.getAuthorIdent().toString();
        committer = commit.getCommitterIdent().toString();
        fullMessage = commit.getFullMessage();
        committerCommitTime = Instant.ofEpochMilli(commit.getCommitTime() * 1000L)
                .atZone(commit.getCommitterIdent().getTimeZone().toZoneId())
                .toLocalDateTime();
        authorCommitTime = Instant.ofEpochMilli(commit.getCommitTime() * 1000L)
                .atZone(commit.getAuthorIdent().getTimeZone().toZoneId())
                .toLocalDateTime();
    }

    public String getAuthor() {
        return author;
    }

    public String getCommitter() {
        return committer;
    }

    public String getFullMessage() {
        return fullMessage;
    }

    public LocalDateTime getCommitterCommitTime() {
        return committerCommitTime;
    }

    public LocalDateTime getAuthorCommitTime() {
        return authorCommitTime;
    }

    @Override
    public String toString() {
        return "Commit{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", committer='" + committer + '\'' +
                ", fullMessage='" + fullMessage + '\'' +
                ", committerCommitTime=" + committerCommitTime +
                ", authorCommitTime=" + authorCommitTime +
                '}';
    }
}
