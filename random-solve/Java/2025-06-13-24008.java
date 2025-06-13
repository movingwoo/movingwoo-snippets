import java.io.*;
import java.util.*;

public class Main {
    
	public static void main(String[] args) {
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out))){
			
			// 모든 입력이 정확히 개수만큼 들어오는게 아님... 공백도 있을 수 있음
			StringTokenizer st = null;
			int T = Integer.parseInt(br.readLine().trim());
			
			for (int t = 1 ; t <= T ; t++) {
				
				int[] nmp = new int[3];
				int idx = 0;
				
				// 앞선 테스트케이스 토큰 있으면 털어주기
				while (st != null && st.hasMoreTokens()) {
					nmp[idx++] = Integer.parseInt(st.nextToken());
				}
				
				while(idx < 3) {
					if (st == null || !st.hasMoreTokens()) st = new StringTokenizer(br.readLine().trim()," ");
					nmp[idx++] = Integer.parseInt(st.nextToken());
				}
				
				
				// 그래프 초기화
				int[][] dist = new int[nmp[0] + 1][nmp[0] + 1];
				
				for( int i = 1 ; i <= nmp[0] ; i++) {
					for (int j = 1 ; j <= nmp[0] ; j++) {
						// U V D가 크지않아서 적당히 큰 값 세팅
						dist[i][j] = i == j ? 0 : Integer.MAX_VALUE / 2;;
					}
				}
				
				// 간선 등록
				// 토큰이 남아있다면 털어주기
				int[] edge = new int[nmp[1] * 3];
				idx = 0;
				
				while(st.hasMoreTokens()) {
					edge[idx++] = Integer.parseInt(st.nextToken());
				}
				
				while (idx < nmp[1] * 3) {
					if (!st.hasMoreTokens()) st = new StringTokenizer(br.readLine().trim(), " ");
					edge[idx++] = Integer.parseInt(st.nextToken());
				}
				
				for (int i = 0 ; i < edge.length ; i += 3) {
					int U = edge[i];
					int V = edge[i + 1];
					int D = edge[i + 2];
					
					// 양방향
					dist[U][V] = D;
					dist[V][U] = D;
				}
				
				
				// 플로이드 워셜 알고리즘
				// 경유지 k가 가장 바깥
				for (int k = 1; k <= nmp[0] ; k++) {
					for (int i = 1 ; i <= nmp[0] ; i++) {
						for (int j = 1 ; j <= nmp[0] ; j++) {
							// k 를 경유해서 가는게 더 빠르면 갱신
							dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]) ;
						}
					}
				}
				
				// 시간 계산
				// 첫번째는 반드시 1번에서 출발
				double start = 0.0;
				for (int i = 2 ; i <= nmp[0] ; i++) {
					start += dist[1][i];
				}
				start /= (double)(nmp[0] - 1);
				
				// 같은 위치 제외 모든 거리의 평균
				double result = 0.0;
				for (int i = 1 ; i <= nmp[0] ; i++) {
					for (int j = 1 ; j <= nmp[0] ; j++) {
						if (i == j) continue;
						
						result += dist[i][j];
					}
				}
				// N * (N - 1)
				result /= (nmp[0] * (double)(nmp[0] - 1));
				
				// 근사값 계산했더니 틀려서 상세하게 접근
				// 람다
				double lambda = -1.0 / (nmp[0] - 1);
				// 기하급수 합
				double geoSum = (1 - Math.pow(lambda, nmp[2])) / (1 - lambda);
				
				result = result * nmp[2] + (start - result) * geoSum;
				
				bw.write(String.format("Case #%d: %.6f\n", t, result));
			}
			
			bw.flush();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
