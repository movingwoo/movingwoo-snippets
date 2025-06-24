import java.io.*;
			
public class Main{
	
    public static void main(String[] args) throws IOException{

    	try(BufferedReader br = new BufferedReader( new InputStreamReader( System.in)); 
			BufferedWriter bw = new BufferedWriter( new OutputStreamWriter( System.out))){
    		
    		char[] arr = br.readLine().toCharArray();
    		int count = 0;
    		
    		for (char c : arr) {
    			if (c == 'T' || c == 'D' || c == 'L' || c == 'F') {
    				count++;
    			}
    		}
    		
    		bw.write(String.format("%.0f", Math.pow(2, count)));
			
		}catch(Exception e) {e.printStackTrace();}
    }
}
