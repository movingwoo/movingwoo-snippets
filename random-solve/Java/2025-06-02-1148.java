import java.io.*;
import java.util.*;

public class Main {
	
    public static void main(String[] args) {
    	
    	try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out))){
    		
    		List<Word> dictionary = new ArrayList<>();
    		
    		// 사전 채우기
            while (true) {
                String word = br.readLine();
                if (word.charAt(0) == '-') break;

                dictionary.add(new Word(word));
            }

            StringBuilder sb = new StringBuilder();

            while (true) {
                String puzzle = br.readLine();
                if (puzzle.charAt(0) == '#') break;

                // 퍼즐판 비트마스크 변환
                int[] puzzleCount = new int[26];
                int puzzleMask = 0;
                
                for (char c : puzzle.toCharArray()) {
                	puzzleCount[c - 'A']++;
                    puzzleMask |= (1 << (c - 'A'));
                }

                Map<Character, Integer> center = new HashMap<>();

                // 중심글자 하나씩 넣어보기
                for (char c : puzzle.toCharArray()) {
                    int centerMask = (1 << (c - 'A'));
                    int count = 0;

                    for (Word w : dictionary) {
                        // 알파벳 체크
                        if ((w.mask & puzzleMask) != w.mask) continue;
                        // 중심글자 체크
                        if ((w.mask & centerMask) == 0) continue;

                        boolean valid = true;
                        // 개수 체크
                        for (int i = 0; i < 26; i++) {
                            if (w.count[i] > puzzleCount[i]) {
                                valid = false;
                                break;
                            }
                        }

                        if (valid) count++;
                    }

                    center.put(c, count);
                }

                // 최소값 최대값 찾기
                int min = Collections.min(center.values());
                int max = Collections.max(center.values());

                List<Character> minLetters = new ArrayList<>();
                List<Character> maxLetters = new ArrayList<>();

                for (Map.Entry<Character, Integer> e : center.entrySet()) {
                    if (e.getValue() == min && !minLetters.contains(e.getKey())) {
                        minLetters.add(e.getKey());
                    }

                    if (e.getValue() == max && !maxLetters.contains(e.getKey())) {
                        maxLetters.add(e.getKey());
                    }
                }

                // 알파벳 순 정렬
                Collections.sort(minLetters);
                Collections.sort(maxLetters);

                for (char ch : minLetters) sb.append(ch);
                sb.append(" ").append(min).append(" ");
                for (char ch : maxLetters) sb.append(ch);
                sb.append(" ").append(max).append("\n");
            }

            bw.write(sb.toString());
            bw.flush();
			
    	} catch (IOException e) {}
    }
    
    // Word class
    static class Word {
        int mask;
        int[] count;

        Word() {
            this.count = new int[26];
        }

        Word(String word) {
            this();
            
            for (char c : word.toCharArray()) {
            	// 무조건 대문자로 들어옴
                count[c - 'A']++;
                mask |= (1 << (c - 'A'));
            }
        }
    }
}
