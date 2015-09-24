public class dataGen {
	public static void main(String[] args)
	{
		int i,k = 1;
		for(i = 0; i <20; i++)
		{
			for(int j = 0;j < 5;j++)
			{
				if(j != 4)
					System.out.print(k+",");
				else
					System.out.print(k);
				k++;
			}
			System.out.println();
		}
	}

}
