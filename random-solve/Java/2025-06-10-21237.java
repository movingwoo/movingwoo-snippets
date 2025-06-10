import java.io.*;
import java.util.*;

public class Main {
    
	public static void main(String[] args) {
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out))){
			
			int N = Integer.parseInt(br.readLine());
			
			// 방향
			Map<Character, Integer> dx = new HashMap<>();
			dx.put('N', 0);
			dx.put('E', 1);
			dx.put('S', 0);
			dx.put('W', -1);
			
			Map<Character, Integer> dy = new HashMap<>();
			dy.put('N', 1);
			dy.put('E', 0);
			dy.put('S', -1);
			dy.put('W', 0);
			
			while(N-- > 0) {
				char[] fences = br.readLine().toCharArray();
				
				int x = 0;
				int y = 0;
				int result = 0;
				
				for(int i = 0 ; i < fences.length ; i++) {
					int nx = x + dx.get(fences[i]);
					int ny = y + dy.get(fences[i]);

          // 신발끈 공식
					result += x * ny - y * nx;

          // 이전 좌표 갱신
					x = nx;
					y = ny;
				}

        // 음수면 시계 방향 아니면 반시계 방향
				if(result < 0) bw.write("CW\n");
				else bw.write("CCW\n");
			}
			
			bw.flush();
			
		}catch(Exception e) {
		}
	}
}
