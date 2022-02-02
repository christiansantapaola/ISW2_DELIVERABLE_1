package it.uniroma2.dicii.santapaola.christian.utility;

/**
 * Class containing the most common statistics function like mean, variance, etc.
 */
public class Statistics {

    /* private constructor */
    private Statistics() {}
    /**
     * compute mean of the given dataset.
     * @param dataset Iterable<Double> input
     * @return
     */
    public static double computeMean(Iterable<Double> dataset) {
        int size = 0;
        double result = 0;
        for (double datapoint: dataset) {
            result += datapoint;
            size++;
        }
        if (size == 0) return 0;
        return result / size;
    }

    /**
     * compute the variance (biased) of the input dataset.
     * @param dataset
     * @return
     */
    public static double computeVariance(Iterable<Double> dataset) {
        double mean = Statistics.computeMean(dataset);
        double size = 0;
        double result = 0;
        for (double datapoint : dataset) {
            result += (datapoint - mean) * (datapoint - mean);
            size++;
        }
        return result / (size - 1);
    }

    /**
     * compute the std.variance of the input dataset.
     * @param dataset
     * @return
     */
    public static double computeStandardVariance(Iterable<Double> dataset) {
        return Math.sqrt(computeVariance(dataset));
    }
}
