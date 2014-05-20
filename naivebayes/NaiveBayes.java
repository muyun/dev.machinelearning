package naivebayes;

import java.util.Arrays;


public class NaiveBayes {

    public static void main(String[] args) {

        /*
         * Create a new classifier instance. The context features are
         * Strings and the context will be classified with a String according
         * to the feature set of the context.
         */
        final Classifier<String, String> bayes =
                new BayesClassifier<String, String>();

        /*
         * The classifier can learn from classifications that are handed over
         * to the learn methods. 
         * The tokens are the text's features. 
         * The category of the text will either be positive or negative.
         */
        final String[] positiveText = "I love sunny days".split("\\s");
        bayes.learn("positive", Arrays.asList(positiveText));

        final String[] negativeText = "I hate rain".split("\\s");
        bayes.learn("negative", Arrays.asList(negativeText));

        /*
         * Now that the classifier has "learned" two classifications, it will
         * be able to classify similar sentences. 
         * The classify method returns a Classification Object, that contains the given feature set,
         * classification probability and resulting category.
         */
        final String[] unknownText1 = "today is a sunny day".split("\\s");
        final String[] unknownText2 = "there will be rain".split("\\s");

        System.out.println(bayes.classify(Arrays.asList(unknownText1)).getCategory()); // will output "positive"
        System.out.println(bayes.classify(Arrays.asList(unknownText1)).getProbability());
        System.out.println(bayes.classify(Arrays.asList(unknownText2)).getCategory());// will output "negative"
        System.out.println(bayes.classify(Arrays.asList(unknownText2)).getProbability());

        /*
         * The BayesClassifier extends the abstract Classifier and provides
         * detailed classification results that can be retrieved by calling
         * the classifyDetailed Method.
         *
         * The classification with the highest probability is the resulting
         * classification. The returned List will look like this.
         * [
         *   Classification [
         *     category=negative,
         *     probability=0.0078125,
         *     featureset=[today, is, a, sunny, day]
         *   ],
         *   Classification [
         *     category=positive,
         *     probability=0.0234375,
         *     featureset=[today, is, a, sunny, day]
         *   ]
         * ]
         */
        ((BayesClassifier<String, String>) bayes).classifyDetailed(
                Arrays.asList(unknownText1));

        /*
         * Please note, that this particular classifier implementation will
         * "forget" learned classifications after a few learning sessions. The
         * number of learning sessions it will record can be set as follows:
         */
        bayes.setMemoryCapacity(500); // remember the last 500 learned classifications
    }

}
