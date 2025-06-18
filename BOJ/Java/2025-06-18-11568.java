import java.io.*;
import java.util.*;
			
public class Main{
    public static void main(String[] args){
		try(BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
			BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(System.out))){
				
			int N=Integer.parseInt(br.readLine());
			int[] arr = new int[N];
			int[] dp = new int[N];
			StringTokenizer st=new StringTokenizer(br.readLine()," ");
			for (int i = 0 ; i < N ; i++) {
				arr[i]=Integer.parseInt(st.nextToken());
			}
			
			// DP
			for (int i = 0 ; i < N ; i++) {
				dp[i] = 1;
				for (int j = 0 ; j < i ; j++) {
					dp[i] = arr[j] < arr[i] ? Math.max(dp[i], dp[j] + 1) : dp[i];
				}
			}
			
			int result = 0;
			for (int i = 0 ; i < N ; i++) {
				// 가장 긴 부분수열
				result = Math.max(result, dp[i]);
			}
			
			bw.write(String.valueOf(result));
			bw.flush();

		}catch(Exception e){e.printStackTrace();}
	}
}
