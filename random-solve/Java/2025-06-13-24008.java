import java.io.*;
import java.util.*;

public class Main {
    
	public static void main(String[] args) {
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out))){
			
			int T = Integer.parseInt(br.readLine().trim());
			
			for (int t = 1 ; t <= T ; t++) {
				
				StringTokenizer st = new StringTokenizer(br.readLine().trim(), " ");
				int N = Integer.parseInt(st.nextToken());
				int M = Integer.parseInt(st.nextToken());
				int P = Integer.parseInt(st.nextToken());
				
				// 그래프 초기화
				int[][] dist = new int[N + 1][N + 1];
				
				for( int i = 1 ; i <= N ; i++) {
					for (int j = 1 ; j <= N ; j++) {
						// U V D가 크지않아서 적당히 큰 값 세팅
						dist[i][j] = i == j ? 0 : 100000;
					}
				}
				
				// 간선 등록
				for (int i = 0 ; i < M ; i++) {
					st = new StringTokenizer(br.readLine().trim(), " ");
					
					int U = Integer.parseInt(st.nextToken());
					int V = Integer.parseInt(st.nextToken());
					int D = Integer.parseInt(st.nextToken());
					
					// 양방향
					dist[U][V] = D;
					dist[V][U] = D;
				}
				
				
				// 플로이드 워셜 알고리즘
				// 경유지 k가 가장 바깥
				for (int k = 1; k <= N ; k++) {
					for (int i = 1 ; i <= N ; i++) {
						for (int j = 1 ; j <= N ; j++) {
							// k 를 경유해서 가는게 더 빠르면 갱신
							dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]) ;
						}
					}
				}
				
				// 시간 계산
				// 첫번째는 반드시 1번에서 출발
				double start = 0.0;
				for (int i = 2 ; i <= N ; i++) {
					start += dist[1][i];
				}
				start /= (double)(N - 1);
				
				// 같은 위치 제외 모든 거리의 평균
				double result = 0.0;
				for (int i = 1 ; i <= N ; i++) {
					for (int j = 1 ; j <= N ; j++) {
						if (i == j) continue;
						
						result += dist[i][j];
					}
				}
				// N * (N - 1)
				result /= (N * (double)(N - 1));
				
				// 근사값 계산했더니 틀려서 상세하게 접근
				// 람다
				double lambda = -1.0 / (N - 1);
				// 기하급수 합
				double geoSum = (1 - Math.pow(lambda, P)) / (1 - lambda);
				
				result = result * P + (start - result) * geoSum;
				
				bw.write(String.format("Case #%d: %.6f\n", t, result));
			}
			
			bw.flush();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
