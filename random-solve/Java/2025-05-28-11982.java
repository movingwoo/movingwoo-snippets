import java.io.*;
import java.util.*;

public class Main {
	
    public static void main(String[] args) throws IOException {

    	try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out))) {
	        
    		int N = Integer.parseInt(br.readLine());
    		long[] hayBales = new long[N];
    		
            for (int i = 0; i < N; i++) {
            	// 소숫점 처리를 위해 2배 스케일링 
            	hayBales[i] = Long.parseLong(br.readLine()) * 2;
            }

            // 오름차순
            Arrays.sort(hayBales);

            long[] leftDP = new long[N];
            long[] rightDP = new long[N];
            // 큰 값으로 초기화 (2 더하면 오버플로우남)
            Arrays.fill(leftDP, Long.MAX_VALUE - 2);
            Arrays.fill(rightDP, Long.MAX_VALUE - 2);
            
            // 2배 스케일링해서 2씩 조절함
            // 왼쪽 전파 DP
            leftDP[0] = -2;  
            int last = 0;
            for (int i = 1; i < N; i++) {
                // last를 오른쪽으로 밀며 폭발이 닿을 때까지
                while (last + 1 < i && hayBales[i] - hayBales[last+1] > leftDP[last+1] + 2) {
                	last++;
                }
                // hayBales 직접 연결 범위, DP[last+1]에 한 단계 추가한 값 중 작은 쪽
                leftDP[i] = Math.min(hayBales[i] - hayBales[last], leftDP[last+1] + 2);
            }
            
            // 오른쪽 전파 DP
            rightDP[N-1] = -2;
            last = N - 1;
            for (int i = N - 2; i >= 0; i--) {
            	// 왼쪽으로 밀며 체크
                while (last - 1 > i && hayBales[last-1] - hayBales[i] > rightDP[last-1] + 2) {
                	last--;
                }
                
                // 한 단계 줄인 값과 비교
                rightDP[i] = Math.min(hayBales[last] - hayBales[i], rightDP[last-1] + 2);
            }

            // 두 포인터로 분할점 i < j를 탐색하며 최적 반경을 찾음
            long best = Long.MAX_VALUE;
            
            int i = 0;
            int j = N - 1;
            while (i < j) {
                // i~j 구간의 가운데와 전파에 필요한 직경 비교
                long need = Math.max((hayBales[j] - hayBales[i]) / 2, Math.max(leftDP[i], rightDP[j]) + 2);
                best = Math.min(best, need);

                // 어느 쪽 DP가 더 작은가에 따라 포인터 이동
                if (leftDP[i+1] < rightDP[j-1]) {
                    i++;
                } else {
                    j--;
                }
            }

            // 2배 스케일링 된 상태이므로 나눠서 소숫점 붙이기
            bw.write(best % 2 == 0 ? String.format("%d.0\n", best/2) : String.format("%d.5\n", best/2));
            bw.flush();
            
	    }catch(IOException e) {
	        e.printStackTrace();
	    }
    }
    
}
