import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class bTreeTimeCalculation {
	static FileReader f;
	static FileWriter w;
	static BufferedWriter wr;
	static BufferedReader br;
	static ArrayList<String> arr;
	static BTree<String, String> stmt;
	public static void btree()
	{
		open();
		String st,op;
		try {
			while((st = br.readLine()) != null)
			{
				op=getNext(st);
				if(op == null)
				{

				    stmt.put(st, "");	
					arr.add(st);
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
			e.printStackTrace();
		}
		close();
	}

	private static void open() {

		try {
			f= new FileReader("ip.txt");
			try {
				stmt  = new BTree<String, String>();
				arr =  new ArrayList<String>();
				w=new FileWriter("op.txt");
				wr = new BufferedWriter(w);
			} catch (IOException e) {
				e.printStackTrace();
			}
			br =  new BufferedReader(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void close() {
		try {
			br.close();
			wr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			f.close();
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static String getNext(String st) {
		 
		 if(stmt.get(st) == null)
			 return null;
		 else
			 return st;
	}


}
