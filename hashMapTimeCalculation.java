import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class hashMapTimeCalculation {
	static FileReader f;
	static FileWriter w;
	static BufferedWriter wr;
	static BufferedReader br;
	static HashMap<String,Boolean> hm;
	static ArrayList<String> arr;
	public static void hash()
	{

		open();
		String st,op;
		try {
			while((st = br.readLine()) != null)
			{
				op=getNext(st);
				if(op != null)
				{
					hm.put(st,Boolean.TRUE);
					arr.add(op);
					if(arr.size() == 5)
					{
						for(int i=0;i<5;i++)
						{
							wr.write(arr.get(i));
							wr.newLine();
						}
						arr.clear();
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		close();
	}

	private static String getNext(String st) {
		if(hm.containsKey(st))
			return null;
		else
		{
			return st;
		}
	}

	private static void close() {
		try {
			br.close();
			wr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			f.close();
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	private static void open() {

		try {
			f= new FileReader("ip.txt");
			try {
				hm = new HashMap<String,Boolean>();
				arr =  new ArrayList<String>();
				w=new FileWriter("op.txt");
				wr = new BufferedWriter(w);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			br =  new BufferedReader(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
