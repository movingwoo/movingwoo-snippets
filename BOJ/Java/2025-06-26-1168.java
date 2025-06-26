import java.io.*;
import java.util.*;

public class Main{
	
    public static void main(String[] args) {
    	
    	try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out))){
    		
    		StringTokenizer st = new StringTokenizer(br.readLine()," ");
    		int N = Integer.parseInt(st.nextToken());
    		int K = Integer.parseInt(st.nextToken());
    		
    		SegmentTree tree = new SegmentTree(N);
    		StringBuilder sb = new StringBuilder();
    		sb.append("<");
    		int idx = 0;
    		
    		for (int i = 0 ; i < N ; i++) {
				// 살아있는 사람들 중 K번 위치의 사람
    			idx = (idx + K - 1) % tree.getAlive();
    			
				// K번 타겟의 실제 위치
    			int target = tree.getDead(idx + 1);
    			
    			if (sb.length() == 1) {
    				sb.append(target + 1);
    			}else {
    				sb.append(", ").append(target + 1);
    			}
    			
    			// 타겟 제거
    			tree.update(target, 0);
    		}
    		
    		sb.append(">");
    		bw.write(sb.toString());
    		bw.flush();
    		
    	}catch(Exception e) {}
    	
    	
    }
    
    public static class SegmentTree {
    	int N;
    	private int size;
    	private int[] tree;
    	
    	public SegmentTree(){
    		this.size = 1;
    	}

    	public SegmentTree(int N){
    		this();
    		this.N = N;
    		
    		while(size < N) {
    			size <<= 1;
    		}
    		
    		tree = new int[size * 2];
    		
    		for (int i = 0 ; i < N ; i++) {
    			tree[size + i] = 1;
    		}
    		
    		for (int i = size - 1 ; i > 0 ; i--) {
    			tree[i] = tree[i * 2] + tree[i * 2 + 1];
    		}
    	}
    	
    	
    	public int getAlive() {
			// 현재 살아있는 사람 수 == 루트 노드
    		return this.tree[1];
    	}
    	
    	
    	public int getDead(int K) {
			int idx = 1;
			
			while (idx < size) {
				
				if(tree[idx * 2] >= K) {
					// 왼쪽 노드 생존자 수가 K이상이면 왼쪽으로
					idx *= 2;
				}else {
					// 왼쪽 노드 생존자 수가 K보다 작으면 오른쪽으로
					K -= tree[idx * 2];
					idx = idx * 2 + 1;
				}
			}
			
			return idx - size;
		}
    	
    	
    	public void update(int idx, int K) {
    		int i = size + idx;
    		tree[i] = K;
    		
    		// 루트까지 업데이트
    		while(i > 1) {
    			i >>= 1;
    			tree[i] = tree[i * 2] + tree[i * 2 + 1];
    		}
    	}
    	
    }
    
}
