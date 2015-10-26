package sw705e15.wi;

import java.util.ArrayList;
import java.util.Comparator;

import org.jblas.DoubleMatrix;
import org.jblas.Eigen;

import sw705e15.wi.FriendshipsParser.FriendshipsData;

public class Scissor
{
	public static FriendshipsData[] Cut(final FriendshipsData data)
	{
		//System.out.println("Step 1!");

		final DoubleMatrix adjacency = new DoubleMatrix(data.friendsMatrix);
		final DoubleMatrix degree = new DoubleMatrix(data.friendsMatrix.length, data.friendsMatrix.length);

		for (int rowIndexCounter = 0; rowIndexCounter < adjacency.rows; rowIndexCounter++)
		{
			double sum = 0;

			for (int columnIndexCounter = 0; columnIndexCounter < adjacency.columns; columnIndexCounter++)
			{
				sum += adjacency.get(rowIndexCounter, columnIndexCounter);
			}

			degree.put(rowIndexCounter, rowIndexCounter, sum);
		}

		//System.out.println("Step 2!");

		final DoubleMatrix laplacian = degree.sub(adjacency);

		final DoubleMatrix eigenvectors = Eigen.symmetricEigenvectors(laplacian)[0];

		//System.out.println("Step 3!");

		if(eigenvectors.columns == 1)
		{
			throw new UnableToSplitFurtherException();
		}
		
		final DoubleMatrix THEEigenvector = eigenvectors.getColumn(1);
		
		final ArrayList<Pair<Integer, Double>> vectorValuesWithOriginalIndexes = new ArrayList<>();

		for (int rowIndexCounter = 0; rowIndexCounter < THEEigenvector.length; rowIndexCounter++)
		{
			vectorValuesWithOriginalIndexes.add(new Pair<Integer, Double>(rowIndexCounter, THEEigenvector.get(rowIndexCounter)));
		}

		vectorValuesWithOriginalIndexes.sort(new Comparator<Pair<Integer, Double>>()
		{
			@Override
			public int compare(final Pair<Integer, Double> o1, final Pair<Integer, Double> o2)
			{
				return (int) ((o1.item2 * 10000000) - (o2.item2 * 10000000));
			}
		});

		Pair<Integer, Double> biggestDiffPair1 = null;
		Pair<Integer, Double> biggestDiffPair2 = null;

		double biggestDiff = 0;
		int splitIndex = 0;

		for (int indexCounter = 0; indexCounter < vectorValuesWithOriginalIndexes.size() - 1; indexCounter++)
		{
			final Pair<Integer, Double> currentPair = vectorValuesWithOriginalIndexes.get(indexCounter);
			final Pair<Integer, Double> nextPair = vectorValuesWithOriginalIndexes.get(indexCounter + 1);

			if (Math.abs(currentPair.item2 - nextPair.item2) > biggestDiff)
			{
				biggestDiffPair1 = currentPair;
				biggestDiffPair2 = nextPair;

				biggestDiff = Math.abs(currentPair.item2 - nextPair.item2);

				splitIndex = indexCounter;
			}
		}

		//System.out.println("Step 4!");
		System.out.println("The cut should be between user: '" + data.indexToNameMapping.get(biggestDiffPair1.item1) + "' and user: '"
				+ data.indexToNameMapping.get(biggestDiffPair2.item1) + "'");
		
		final FriendshipsData cluster1 = splitFromIndexToIndexWithData(0, splitIndex + 1, vectorValuesWithOriginalIndexes, data);
		final FriendshipsData cluster2 = splitFromIndexToIndexWithData(splitIndex + 1, vectorValuesWithOriginalIndexes.size(), vectorValuesWithOriginalIndexes,
				data);

		return new FriendshipsData[]{cluster1, cluster2};
	}

	public static FriendshipsData splitFromIndexToIndexWithData(final int startIndex, final int endIndex,
			final ArrayList<Pair<Integer, Double>> sortedEigenMatrix, final FriendshipsData data)
	{
		final FriendshipsData cluster = new FriendshipsData(data.indexToNameMapping.size() / 2);

		for (int clusterIndex = startIndex; clusterIndex < endIndex; clusterIndex++)
		{
			final Pair<Integer, Double> currentPair = sortedEigenMatrix.get(clusterIndex);

			final String name = data.indexToNameMapping.get(currentPair.item1);
			cluster.indexToNameMapping.add(name);
			cluster.nameToIndexMapping.put(name, cluster.indexToNameMapping.size() - 1);
		}
		
		cluster.friendsMatrix = new double[cluster.indexToNameMapping.size()][cluster.indexToNameMapping.size()];
		
		for(int rowsIndexCounter = 0; rowsIndexCounter < cluster.indexToNameMapping.size(); rowsIndexCounter++)
		{
			final String outername = cluster.indexToNameMapping.get(rowsIndexCounter);
			final int originalDataRowIndex = data.nameToIndexMapping.get(outername);
			
			for(int columnsIndexCounter = 0; columnsIndexCounter < cluster.indexToNameMapping.size(); columnsIndexCounter++)
			{
				final String innername = cluster.indexToNameMapping.get(columnsIndexCounter);
				final int originalDataColumnIndex = data.nameToIndexMapping.get(innername);
				
				if(data.friendsMatrix[originalDataRowIndex][originalDataColumnIndex] > 0)
				{
					cluster.friendsMatrix[rowsIndexCounter][columnsIndexCounter] = 1;
				}
			}
		}
		
		return cluster;
	}
	
	public static class UnableToSplitFurtherException extends IllegalArgumentException
	{
		
	}
}
