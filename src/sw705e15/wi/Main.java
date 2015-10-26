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

import sw705e15.wi.FriendshipsParser.FriendshipsData;
import sw705e15.wi.Scissor.UnableToSplitFurtherException;

public class Main
{

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

		// final DoubleMatrix A = DoubleMatrix.rand(4000, 4000);
		/*
		 * final DoubleMatrix A = new DoubleMatrix( new double[][]{ {0, 4, 3, 0,
		 * 0, 0}, {4, 0, 2, 0, 0, 0}, {3, 2, 0, 2, 0, 0}, {0, 0, 2, 0, 2, 3},
		 * {0, 0, 0, 2, 0, 4}, {0, 0, 0, 3, 4, 0}, });
		 */
		/*
		 * final DoubleMatrix D = new DoubleMatrix( new double[][]{ {7, 0, 0, 0,
		 * 0, 0}, {0, 6, 0, 0, 0, 0}, {0, 0, 7, 0, 0, 0}, {0, 0, 0, 7, 0, 0},
		 * {0, 0, 0, 0, 6, 0}, {0, 0, 0, 0, 0, 7}, });
		 */

		// final DoubleMatrix D = DoubleMatrix.rand(4000, 4000);

		// final DoubleMatrix L = D.sub(A);

		// System.out.println(L.toString().replace(";", "\n"));

		// DoubleMatrix bountifulPenis = Eigen.symmetricEigenvectors(L)[0];

		// System.out.println("DONE");
		// System.out.println(bountifulPenis.toString().replace(";", "\n"));

		// System.out.println(Eigen.symmetricEigenvalues(L).toString().replace(";",
		// "\n"));

		FriendshipsData data = null;

		try
		{
			data = FriendshipsParser.parseFriendshipsFile();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<FriendshipsData> clusters = new ArrayList<>();
		clusters.add(data);
		List<FriendshipsData> tempClusters;

		for (int recursionLevelsCounter = 0; recursionLevelsCounter < 6; recursionLevelsCounter++)
		{
			tempClusters = new ArrayList<>();

			for (FriendshipsData cluster : clusters)
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
	}

}
