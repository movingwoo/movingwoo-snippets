import java.util.*;

public class Main {
    
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		int T = Integer.parseInt(sc.nextLine());
		
		while(T-- > 0) {
			char[] p = sc.nextLine().toCharArray();
			sc.nextLine(); // n은 쓸데없음
			String x = sc.nextLine();
			x = x.substring(1, x.length() - 1); // 앞뒤 대괄호 제거
			
			// deque
			Deque<Integer> deque = new LinkedList<>();
			
			if(!x.isEmpty()) {
				StringTokenizer st = new StringTokenizer(x, ",");
				while(st.hasMoreTokens()) {
					deque.add(Integer.parseInt(st.nextToken()));
				}
			}
			
			boolean front = true;
			boolean error = false;
			
			for(char oper : p) {
				if (oper == 'R') {
					front = !front;
				}
				
				// poll 할때만 error check
				if (oper == 'D') {
					if (deque.size() < 1) {
						error = true;
						break;
					}
					
					if (front) deque.pollFirst();
					else deque.pollLast();
				}
			}
			
			if (error) System.out.println("error");
			else {
				StringBuilder sb = new StringBuilder();
				sb.append("[");
				
				// deque 출력
				while(!deque.isEmpty()) {
					sb.append((sb.length() == 1 ? "" : ","));
					if (front) sb.append(deque.pollFirst());
					else sb.append(deque.pollLast());
				}
				
				sb.append("]");
				System.out.println(sb.toString());
			}
		}
		
		sc.close();
		
	}
}
