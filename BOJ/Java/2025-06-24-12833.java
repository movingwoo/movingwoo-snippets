import java.io.*;
			
public class Main{
	
    public static void main(String[] args) throws IOException{

    	InputReader ir = new InputReader();
    	
    	int A = ir.nextInt();
    	int B = ir.nextInt();
    	int C = ir.nextInt();
    	
    	if (C % 2 == 0) System.out.println(A);
    	else System.out.println(A ^ B);
	}
    
    // 빠른 입출력 구현
    public static class InputReader {
    	
        private final InputStream in = System.in;
        private final byte[] buf = new byte[1<<20];
        
        private int ptr = 0;
        private int buflen = 0;

        int nextInt() throws IOException {
            int c = 0;
            int x = 0;
            boolean negative = false;
            
            while(true) {
            	c = read();
            	if (c > ' ') break;
            }
            
            if (c == '-') { 
            	negative = true;
            	c = read(); 
            }
            
            while(c >= '0' && c <= '9') {
            	x = x * 10 + (c - '0');
            	c= read();
            }
            
            return negative ? -x : x;
        }

        private int read() throws IOException {
            if (ptr >= buflen) {
                buflen = in.read(buf);
                ptr = 0;
                if (buflen <= 0) {
                	return -1;
                }
            }
            
            return buf[ptr++] & 0xFF;
        }
    }
}
