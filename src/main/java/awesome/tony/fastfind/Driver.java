package awesome.tony.fastfind;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map.Entry;

import awesome.tony.fastfind.ByteArrayAhoCorasick.SearchNugget;

public class Driver {

	static class TallyingCallback implements FindCallback{
		int bytesRead = 0;
		HashMap<String, Integer> m = new HashMap<String, Integer>();
		
		@Override
		public void findCallback(int offsetInCurrentBuffer, byte[] termMatch) {
			String match = new String(termMatch);
			Integer count;
			int newCount;
			if((count = m.get(match)) == null){
				newCount = 1;
			}
			else{
				newCount = count + 1;
			}
			m.put(match, newCount);
		}

		public void updateBytesRead(int bytesRead) {
			this.bytesRead += bytesRead;
		}
	}
	
	public static void main(String args[]){
		try{
			if(args.length != 2){
				System.out.println("Usage: " + Driver.class.toString() + " <file with list of terms> <file to grep>");
				System.exit(1);
			}
			long pre = System.currentTimeMillis();

			FileInputStream fis = new FileInputStream(args[0]);
			BufferedReader terms = new BufferedReader(new InputStreamReader(fis));
			String justRead;
			ByteArrayAhoCorasick bac = new ByteArrayAhoCorasick();
			while((justRead = terms.readLine()) != null){
				bac.addMatch(justRead.getBytes());
			}
			terms.close();
			bac.finalize();
			
			TallyingCallback t = new TallyingCallback();
			SearchNugget n = bac.startMultiCallSearch();
			InputStream match = new BufferedInputStream(new FileInputStream(args[1]));
			byte buffer[] = new byte[4<<10];
			int bytesRead;
			while((bytesRead = match.read(buffer)) != -1){
				bac.evaluate(buffer, 0, bytesRead, n, t);
				t.updateBytesRead(bytesRead);
			}
			match.close();
			long post = System.currentTimeMillis();
			for(Entry<String, Integer> e : t.m.entrySet()){
				System.out.println(e.getKey() + ": " + e.getValue());
			}
			System.out.println("elapsed time: " + (post - pre));
		}
		catch(Exception e){
			e.printStackTrace();
		
		}
	}


}
