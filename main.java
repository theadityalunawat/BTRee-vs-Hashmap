public class main {
	public static void main(String[] args)
	{
		String R = args[0];
		int n = Integer.parseInt(args[1]);
		int M = Integer.parseInt(args[2]);
		String type = args[3];
		if(type.toLowerCase().equals("hash"))
		{
			hashMapTimeCalculation.hash();
		}
		else
		{
		bTreeTimeCalculation.btree();	
		}
	}

}
