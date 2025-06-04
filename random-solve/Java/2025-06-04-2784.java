import java.io.*;
import java.util.*;

public class Main {
	
    public static void main(String[] args) {
    	
    	try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out))){
    		
    		List<String> words = new ArrayList<>();
    		// 비교 시 중복때문에 Set을 못써서 Map 사용
    		Map<String, Integer> count = new HashMap<>();
    		
    		for (int i = 0 ; i < 6 ; i++) {
    			String word = br.readLine();
    			words.add(word);
    			count.put(word, count.getOrDefault(word, 0) + 1);
    		}
    		
    		boolean result = false;
    		
    		exit:
    		for (int i = 0 ; i < 6 ; i++) {
    			for (int j = 0 ; j < 6 ; j++) {
    				if (i == j) continue;
    				for (int k = 0 ; k < 6 ; k++) {
    					if (i == k || j == k) continue;
    					
    					String horizontalWord1 = words.get(i);
    					String horizontalWord2 = words.get(j);
    					String horizontalWord3 = words.get(k);
    					
    					// 만든 단어
    					Map<String, Integer> used = new HashMap<>();
    					
    					int putCount = 0;
    					// 세로 줄 단어 생성
    					for (int l = 0 ; l < 3 ; l++) {
    						String verticalWord = "" + horizontalWord1.charAt(l) + horizontalWord2.charAt(l) + horizontalWord3.charAt(l);
    						
    						// 세로단어가 존재하지 않는 단어면 통과
    						if (!words.contains(verticalWord)) break;
    						
    						used.put(verticalWord, used.getOrDefault(verticalWord, 0) + 1);
    						putCount++;
    					}
    					
    					// 3번 안넣었으면 굳이 더 볼 필요 없음
    					if (putCount < 3) continue;
    					
    					used.put(horizontalWord1, used.getOrDefault(horizontalWord1, 0) + 1);
    					used.put(horizontalWord2, used.getOrDefault(horizontalWord2, 0) + 1);
    					used.put(horizontalWord3, used.getOrDefault(horizontalWord3, 0) + 1);
    					
    					boolean valid = true;
    					// 비교
    					for (Map.Entry<String, Integer> entry : used.entrySet()) {
    						if (entry.getValue() != count.get(entry.getKey())) {
    							valid = false;
    							break;
    						}
    					}
    					
    					if (valid) {
    						result = true;
    						bw.write(horizontalWord1);
    						bw.write("\n");
    						bw.write(horizontalWord2);
    						bw.write("\n");
    						bw.write(horizontalWord3);
    						break exit;
    					}
    				}
    			}
    		}
    		
    		if (!result) bw.write("0");
			
    	} catch (IOException e) {}
    }
    
}
