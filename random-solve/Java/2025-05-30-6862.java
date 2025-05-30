import java.io.*;
import java.util.*;

public class Main {
	
    public static void main(String[] args) {

    	try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out))) {
	        
    		StringTokenizer st = new StringTokenizer(br.readLine()," ");
    		
    		int xR = Integer.parseInt(st.nextToken());
    		int yR = Integer.parseInt(st.nextToken());
    		int xJ = Integer.parseInt(st.nextToken());
    		int yJ = Integer.parseInt(st.nextToken());
    		
    		int n = Integer.parseInt(br.readLine());
    		int count = 0;
    		
    		for (int i = 0 ; i < n ; i++) {
    			st = new StringTokenizer(br.readLine()," ");
    			int length = Integer.parseInt(st.nextToken());
    			Vertex[] vertexes = new Vertex[length];
    			int j = 0;
    			
    			while(st.hasMoreTokens()) {
    				int x = Integer.parseInt(st.nextToken());
    				int y = Integer.parseInt(st.nextToken());
    				
    				vertexes[j++] = new Vertex(x, y);
    			}
    			
    			boolean blocked = false;
    			
    			// 메인루프
    			for(int k = 0 ; k < length ; k++) {
    				
    				// 세 점의 방향성을 확인해 선분 교차 확인
    				long ab1 = counterClockWise(xR, yR, xJ, yJ, vertexes[k].x, vertexes[k].y);
    				long ab2 = counterClockWise(xR, yR, xJ, yJ, vertexes[(k + 1) % length].x, vertexes[(k + 1) % length].y);
    				long cd1 = counterClockWise(vertexes[k].x, vertexes[k].y, vertexes[(k + 1) % length].x, vertexes[(k + 1) % length].y, xR, yR);
    				long cd2 = counterClockWise(vertexes[k].x, vertexes[k].y, vertexes[(k + 1) % length].x, vertexes[(k + 1) % length].y, xJ, yJ);
    				
    				if (ab1 * ab2 < 0 && cd1 * cd2 < 0) {
    					blocked = true;
    					break;
    				}
    				
    				// 꼭지점 접촉 확인
    				if ((ab1 == 0 && onSegment(xR, yR, xJ, yJ, vertexes[k].x, vertexes[k].y)) ||
    					(ab2 == 0 && onSegment(xR, yR, xJ, yJ, vertexes[(k + 1) % length].x, vertexes[(k + 1) % length].y)) ||
    					(cd1 == 0 && onSegment(vertexes[k].x, vertexes[k].y, vertexes[(k + 1) % length].x, vertexes[(k + 1) % length].y, xR, yR)) ||
    					(cd2 == 0 && onSegment(vertexes[k].x, vertexes[k].y, vertexes[(k + 1) % length].x, vertexes[(k + 1) % length].y, xJ, yJ))) {
    					blocked = true;
    					break;
    				}
    				
    				
    			}
    			
    			if (blocked) count++;
    		}
    		
    		bw.write(String.valueOf(count));
    		bw.flush();
            
	    }catch(IOException e) {
	        e.printStackTrace();
	    }
    }
    
    // Counter ClockWise
    static long counterClockWise(int x1, int y1, int x2, int y2, int x3, int y3) {
    	return (long)(x2 - x1) * (y3 - y1) - (long)(y2 - y1) * (x3 - x1);
    }
    
    // 점이 선분위에 있는지 여부
    static boolean onSegment(int x1, int y1, int x2, int y2, int x3, int y3) {
    	return Math.min(x1, x2) <= x3 && x3 <= Math.max(x1, x2) &&
    		   Math.min(y1, y2) <= y3 && y3 <= Math.max(y1, y2);
    }
    
    // 건물 꼭지점 클래스
    static class Vertex{
    	int x;
    	int y;
    	
    	Vertex(int x, int y){
    		this.x = x;
    		this.y = y;
    	}
    }
    
}
