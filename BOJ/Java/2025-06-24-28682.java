import java.io.*;
import java.util.*;
			
public class Main{
	
    public static void main(String[] args) throws IOException{

    	Scanner sc = new Scanner(System.in);
    	StringBuilder sb = new StringBuilder();
    	
    	// 1. swimming 출력
    	// 문제에서 n은 1500이라 명시됨
    	sc.nextLine();
    	
    	sb.append("swimming");
    	for (int i = 0 ; i < 1500 ; i++) sb.append(" swimming");
    	
    	System.out.println(sb.toString());
    	System.out.flush();
    	
    	// 2. 입력에 따라 bowling 또는 soccer 출력
    	StringTokenizer st = new StringTokenizer(sc.nextLine(), " ");
    	sb = new StringBuilder();
    	
    	while (st.hasMoreTokens()) {
    		if (st.nextToken().charAt(0) == 's') sb.append("bowling ");
    		else sb.append("soccer ");
    	}
    	
    	System.out.println(sb.substring(0, sb.length()-1).toString());
    	System.out.flush();
    	
    	sc.close();
    }
}
