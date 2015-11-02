package sw705e15.wi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.jblas.ComplexDoubleMatrix;
import org.jblas.DoubleMatrix;
import org.jblas.Eigen;

import sw705e15.wi.FriendshipsParser.Cluster;
import sw705e15.wi.Scissor.UnableToSplitFurtherException;
import sw705e15.wi.sentiment.NaiveSentimentAnalytic;

public class Main
{
	public static void main(final String[] args)
	{
		new NaiveSentimentAnalytic();
		
		/*
		Cluster data = null;

		try
		{
			data = FriendshipsParser.parseFriendshipsFile();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Cluster> clusters = new ArrayList<>();
		clusters.add(data);
		List<Cluster> tempClusters;

		for (int recursionLevelsCounter = 0; recursionLevelsCounter < 20; recursionLevelsCounter++)
		{
			tempClusters = new ArrayList<>();

			for (Cluster cluster : clusters)
			{
				try
				{
					tempClusters.addAll(Arrays.asList(Scissor.Cut(cluster)));
				}
				catch (UnableToSplitFurtherException e)
				{
					tempClusters.add(cluster);
				}
			}

			clusters = tempClusters;
		}

		return;
		*/
	}

}
