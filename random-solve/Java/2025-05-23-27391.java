import java.io.*;
import java.util.*;

public class Main {
	
    public static void main(String[] args) {
    	
    	try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out))) {
	        
	        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
	        int n = Integer.parseInt(st.nextToken());
	        int s = Integer.parseInt(st.nextToken());
	        int k = Integer.parseInt(st.nextToken());
	        int result = 0;
	        
	        int[] platforms = new int[n];
	        for(int i = 0 ; i < n ; i++) {
	        	platforms[i] = Integer.parseInt(br.readLine());
	        }
	        // 오름차순
	        Arrays.sort(platforms);
	        
	        int[] lengths = new int[n];
	        // 최대길이로 일단 채우고 시작
	        Arrays.fill(lengths, k);
	        
	        for (int i = 1 ; i < n ; i++) {
	        	int dist = platforms[i] - platforms[i - 1];
	        	// 플랫폼이 양쪽으로 펼쳐짐
	            int max = 2 * dist;

	            // 불가능한 경우
	            if (max < 2 * s) {
	                bw.write("-1");
	                return;
	            }

	            // 이전 길이와 현재 길이의 합이 max를 넘지 않도록 조정
	            if (lengths[i - 1] + lengths[i] > max) {
	                lengths[i] = Math.max(s, Math.min(lengths[i], max - lengths[i - 1]));

	                // 여전히 안 되면 lengths[i]를 최대한 줄여서 해결 안되면 length[i-1] 조절을 해야함
	                if (lengths[i - 1] + lengths[i] > max) {
	                    lengths[i - 1] = Math.max(s, max - lengths[i]);
	                }
	            }
	        }
	        
	        for (int length : lengths) result += length;
	        bw.write(String.valueOf(result));
	        
	    }catch(IOException e) {
	        e.printStackTrace();
	    }
    }
}
