import java.io.*;
import java.util.*;
			
public class Main{
    public static void main(String[] args){
		try(BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
			BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(System.out))){

			int n=Integer.parseInt(br.readLine());
			StringTokenizer st=new StringTokenizer(br.readLine()," ");
			
			// 처음 입력받을 배열
			int[] arr = new int[n];
			// 중복제거 및 정렬용 트리셋
			Set<Integer> set = new TreeSet<>();
			// 압축 매핑용 맵
			Map<Integer, Integer> map = new HashMap<>();
			
			for (int i = 0 ; i < n ; i++) {
				int token = Integer.parseInt(st.nextToken());
				arr[i] = token;
				set.add(token);
			}
			
			// 맵에 매핑
			Iterator<Integer> iter = set.iterator();
			int idx = 0;
			while (iter.hasNext()) {
				map.put(iter.next(), idx++);
			}
			
			// 출력
			StringBuilder sb = new StringBuilder();
			for (int i = 0 ; i < n ; i++) {
				sb.append(map.get(arr[i])).append(" ");
			}
			
			bw.write(sb.toString().substring(0, sb.length() - 1));
			bw.flush();

		}catch(Exception e){e.printStackTrace();}
	}
}
