import GitSubSystem.Commit;
import org.eclipse.jgit.api.errors.GitAPIException;
import utility.Histogram;
import utility.Statistics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

public class Main {

public static void main(String[] args) {
    try {
        Main main = new Main();
        String jiraProject = "S2GRAPH";
        String giturl = "https://github.com/apache/incubator-s2graph";
        String repositoryFolder = "repository/";
        String outputFolder = "output/";
        File repositoryDirectory = new File(repositoryFolder);
        if (! repositoryDirectory.exists()) {
            repositoryDirectory.mkdir();
        }
        File outputDirectory = new File(outputFolder);
        if (! outputDirectory.exists()) {
            outputDirectory.mkdir();
        }
        File gitPath = new File(repositoryFolder + jiraProject + "/.git/");
        File csvOutput = new File(outputFolder + jiraProject + ".csv");
        RepositoryMiner repositoryMiner = new RepositoryMiner(gitPath, jiraProject);
        main.getProcessControlChart(repositoryMiner, csvOutput);
    } catch (IOException | GitAPIException e) {
        e.printStackTrace();
    }
}


    /**
     * DatePoint class, needed to store just the year and the month of a commit.
     */
    private class DatePoint implements Comparable<DatePoint> {
    int month, year;

    public DatePoint(int month, int year) {
        this.month = month % 12;
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        DatePoint datePoint = (DatePoint) obj;
        return (datePoint.getYear() == this.getYear()) && (datePoint.getMonth() == this.getMonth());
    }

    @Override
    public int compareTo(Main.DatePoint o) {
        if (this == o) return 0;
        if (this.getYear() < o.getYear()) {
            return -1;
        } else if (this.getYear() > o.getYear()) {
            return 1;
        } else {
            if (this.getMonth() < o.getMonth()) {
                return -1;
            } else if (this.getMonth() > o.getMonth()) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}

    /**
     * given a project, return the process controll chart of the project, and write it in csvOutput.
     * @param repositoryMiner
     * @param csvOutput
     * @throws IOException
     * @throws GitAPIException
     */
    private void getProcessControlChart(RepositoryMiner repositoryMiner, File csvOutput) throws IOException, GitAPIException {
        List<Commit> commits = repositoryMiner.getBugCommit();
        BufferedWriter csvWriter = new BufferedWriter(new FileWriter(csvOutput));
        Histogram<DatePoint> histogram = new Histogram<>();
        for (Commit commit : commits) {
            DatePoint dp = new DatePoint(commit.getCommitterCommitTime().getMonthValue(),
                    commit.getCommitterCommitTime().getYear());
            histogram.insert(dp);
        }
        List<Double> NoBug = new ArrayList<Double>();
        for (DatePoint point : histogram.getKeys()) {
            int nobug = histogram.retrieve(point);
            NoBug.add(Double.valueOf(nobug));
        }
        double mean = Statistics.computeMean(NoBug);
        double stdvar = Statistics.computeStandardVariance(NoBug);
        csvWriter.append("date, noBug, mean, lowerLimit, upperLimit\n");
        for (DatePoint point : histogram.getKeys()) {
            csvWriter.append(Integer.toString(point.getYear()));
            csvWriter.append('\\');
            csvWriter.append(Integer.toString(point.getMonth()));
            csvWriter.append(',');
            csvWriter.append(Integer.toString(histogram.retrieve(point)));
            csvWriter.append(',');
            csvWriter.append(Double.toString(mean));
            csvWriter.append(',');
            csvWriter.append(Double.toString(max(0.0,mean - 3 * stdvar)));
            csvWriter.append(',');
            csvWriter.append(Double.toString(mean + 3 * stdvar));
            csvWriter.append('\n');
        }
        csvWriter.flush();
    }


}
