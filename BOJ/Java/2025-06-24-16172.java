import java.io.*;
			
public class Main{
	
    public static void main(String[] args) throws IOException{

    	try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out))){
    		
    		String S = br.readLine();
    		String K = br.readLine();
    		
    		// KMP 배열
    		int[] kmp = new int[K.length()];
    		
    		int idx = 0; 

    		for (int i = 1 ; i < kmp.length ; i++) {
    			
    			if (K.charAt(i) == K.charAt(idx)) {
    				kmp[i] = ++idx; 
    			} else if (idx != 0) {
    				// aabc abc 같은 패턴
    				idx = kmp[idx -1]; 
    				i--;
    			}else {
    				kmp[i] = 0;
    			}
    		}
    		
    		boolean result = false;

    		int j = 0;
    		for (int i = 0 ; i < S.length() ; i++) {
    			if (Character.isLetter(S.charAt(i))) {
    				
    				while(j > 0 && S.charAt(i) != K.charAt(j)) {
    					j = kmp[j - 1];
    				}
    				
    				if (S.charAt(i) == K.charAt(j)) {
    					j++;
    					if (j == K.length()) {
    						result = true;
    					}
    				}
    			}
    		}
    		
    		if (result) bw.write("1");
    		else bw.write("0");
    		
    		bw.flush();
    	}	
    }
}
