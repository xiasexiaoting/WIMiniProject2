package sw705e15.wi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FriendshipsParser
{
	public static class FriendshipsData
	{
		protected HashMap<String, Integer> nameToIndexMapping;
		protected List<String> indexToNameMapping;
		protected double[][] friendsMatrix;
		
		FriendshipsData(final int hint)
		{
			nameToIndexMapping = new HashMap<>(hint);
			indexToNameMapping = new ArrayList<>(hint);
		}
	}

	public static FriendshipsData parseFriendshipsFile() throws IOException
	{
		final FriendshipsData data = new FriendshipsData(5000);
		final BufferedReader inFile = new BufferedReader(new FileReader("data\\friendships.txt"));
		//final BufferedReader inFile = new BufferedReader(new FileReader("data\\newfriendships.txt"));
		final List<ArrayList<String>> friends = new ArrayList<>();

		// int index = 0;
		String line;
		while ((line = inFile.readLine()) != null)
		{
			if (line.contains("user:"))
			{
				// Save a reference to the username
				final String username = line.replace("user: ", "");

				// Create mappings from username to index and reverse
				if (!data.nameToIndexMapping.containsKey(username))
				{
					data.indexToNameMapping.add(username);
					data.nameToIndexMapping.put(username, data.indexToNameMapping.size() - 1);
				}
				else
				{
					System.out.println("DUPLICATES!");
				}
			}
			else if (line.contains("friends:"))
			{
				// We cache friends in a list, we do not know all of their
				// indexes yet and we do not wish to run through the file twice
				// (Disks are slow)
				final String[] friendNames = line.split("\\t");
				final ArrayList<String> friendsForThisUser = new ArrayList<>();

				// We start at index 1 (The first string in the split us
				// "friends:")
				for (int counter = 1; counter < friendNames.length; counter++)
				{
					final String friendName = friendNames[counter];
					friendsForThisUser.add(friendName);
				}

				friends.add(friendsForThisUser);
			}
		}

		// Close file to prevent leak
		inFile.close();

		// Save the total user count
		final int userCounter = data.indexToNameMapping.size();

		// Initialize friends matrix
		data.friendsMatrix = new double[userCounter][userCounter];

		// Run through all friends and populate data.friendsMatrix
		for (int userIndexCounter = 0; userIndexCounter < friends.size(); userIndexCounter++)
		{
			// Find the list of friends for user at index: userIndexCounter
			final ArrayList<String> friendNames = friends.get(userIndexCounter);

			// For each friend: Flip bit in matrix
			for (String friendName : friendNames)
			{
				final Integer friendIndex = data.nameToIndexMapping.get(friendName);

				if (friendIndex != null)
				{
					data.friendsMatrix[userIndexCounter][friendIndex] = 1;
					data.friendsMatrix[friendIndex][userIndexCounter] = 1;
				}
			}
		}

		return data;
	}

}
