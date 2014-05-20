package naivebayes;

/**
 * the interface defines the method to calculate the feature probability.
 *
 * @author wenlong
 *
 * @param <T> feature class.
 * @param <K> category class.
 */
public interface FeatureProbability<T, K> {

    public float featureProbability(T feature, K category);

}
