package sw705e15.wi.sentiment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class NaiveSentimentAnalytic
{
	final HashMap<String, Boolean> trainingData = new HashMap<>();
	final HashMap<String, Boolean> validationData = new HashMap<>();

	final HashSet<String> vocabulary = new HashSet<>();
	final HashMap<String, Integer> positiveCountMap = new HashMap<>();
	final HashMap<String, Integer> negativeCountMap = new HashMap<>();

	private static final double POSITIVE_COUNT = 10;
	private static final double NEGATIVE_COUNT = 10;
	private static final double TOTAL_COUNT = POSITIVE_COUNT + NEGATIVE_COUNT;

	private static final double PROB_POSITIVE = (POSITIVE_COUNT + 1) / (TOTAL_COUNT + 2);
	private static final double PROB_NEGATIVE = (NEGATIVE_COUNT + 1) / (TOTAL_COUNT + 2);

	public NaiveSentimentAnalytic()
	{
		vocabulary.addAll(Arrays.asList(new String[] { "good", "bad", "fine", "nice", "nasty", "awful", "awesome", "shit", "dog", "cat" }));

		trainingData.put("good bad fine nice", true);
		trainingData.put("nasty awful", false);
		trainingData.put("shit cat good nice", true);
		trainingData.put("fine fine nasty cat", true);
		trainingData.put("bad dog", false);

		trainingData.put("bad nasty", false);
		trainingData.put("bad awful", false);
		trainingData.put("bad bad bad", false);
		trainingData.put("bad bad awful", false);
		trainingData.put("dog shit", false);

		trainingData.put("good cat", true);
		trainingData.put("good dog", true);
		trainingData.put("awesome fine cat", true);
		trainingData.put("good nasty shit", false);
		trainingData.put("cat fine good fine", true);

		trainingData.put("good fine nice", true);
		trainingData.put("good good good good", true);
		trainingData.put("fine fine good nice", true);
		trainingData.put("shit dog bad", false);
		trainingData.put("awful shit dog", false);

		for (String vocabularyWord : vocabulary)
		{
			positiveCountMap.put(vocabularyWord, N(vocabularyWord, true));
			negativeCountMap.put(vocabularyWord, N(vocabularyWord, false));
		}

		validationData.put("good shit", false);

		int correctClassifiedCount = 0;

		for (Map.Entry<String, Boolean> review : validationData.entrySet())
		{
			if (classify(review.getKey()) == review.getValue())
			{
				correctClassifiedCount++;
			}
		}

		System.out.println("Correct classification count: " + correctClassifiedCount);
	}

	public int N(final String word, final boolean classification)
	{
		int count = 0;
		for (Map.Entry<String, Boolean> review : trainingData.entrySet())
		{
			if (review.getValue() == classification)
			{
				final String reviewText = review.getKey();
				count += reviewText.split(word).length - 1;
			}
		}

		return count;
	}

	public double computeEmptyScore(final boolean classification)
	{
		double score = 1;

		for (String vocabularyWord : vocabulary)
		{
			if (classification)
			{
				score *= 1 - ((positiveCountMap.get(vocabularyWord) + 1) / (POSITIVE_COUNT + vocabulary.size()));
			}
			else
			{
				score *= 1 - ((negativeCountMap.get(vocabularyWord) + 1) / (NEGATIVE_COUNT + vocabulary.size()));
			}
		}

		if (classification)
		{
			score *= PROB_POSITIVE;
		}
		else
		{
			score *= PROB_NEGATIVE;
		}

		return score;
	}

	public double computeScore(final String review, final boolean classification)
	{
		final String[] words = review.split("\\s+");

		final double emptyScore = computeEmptyScore(classification);

		double score = 1;

		for (String word : words)
		{
			double wordProb;

			if (classification)
			{
				wordProb = (positiveCountMap.get(word) + 1) / (POSITIVE_COUNT + vocabulary.size());
			}
			else
			{
				wordProb = (negativeCountMap.get(word) + 1) / (NEGATIVE_COUNT + vocabulary.size());
			}

			score *= wordProb / (1 - wordProb);
		}

		return score * emptyScore;
	}

	public boolean classify(final String review)
	{
		final double positiveScore = computeScore(review, true);
		final double negativeScore = computeScore(review, false);

		return positiveScore > negativeScore;
	}
}
