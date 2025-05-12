import java.util.*;
import java.util.regex.*;

public class Main {
	
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // 전체 후보군 설정
        List<String> allCandidates = new ArrayList<>();

        // 최소 0123, 최대 9876
        for (int i = 123; i <= 9876; i++) {
        	String number = String.format("%04d", i);
        	
        	// 스트림으로 짜니까 맛있어짐...
            if (number.chars().distinct().count() == 4) {
            	allCandidates.add(number);
            }
        }

        // 정답으로 추측되는 후보군
        List<String> candidates = new ArrayList<>(allCandidates);
        // 시도한 추측을 저장해서 중복 추측 방지
        Set<String> triedGuesses = new HashSet<>();
        
        int turn = 1;

        while (true) {
        	// 1턴에서는 추측이 의미 없으므로 1234를 제시하고 결과를 받도록 한다.
        	String guess = "1234";
        	
        	if (turn != 1) {
        		guess = chooseBestGuess(candidates, allCandidates, triedGuesses);
        	}

            triedGuesses.add(guess);  // 시도한 추측 기록

            System.out.println(turn + "턴) 추천 추측: " + guess);
            System.out.print("입력 (예: 1s2b / 3s / 2b / o): ");
            // 추천 추측에 대한 결과를 입력받는다.
            String input = sc.nextLine().trim().toLowerCase();

            if (input==null || input.length() == 0) {
            	System.out.println("입력 없음");
            	continue;
            }

            int strikes = 0;
            int balls = 0;

            // 정규식으로 스트라이크 볼 패턴 구분
            Pattern pattern = Pattern.compile("^(\\d+s)?(\\d+b)?$|^(\\d+b)?(\\d+s)?$");
            
            if (input.equals("o")) {
            	// 아웃은 0s0b 따로 할 거 없음
                strikes = 0;
                balls = 0;
            } else if (pattern.matcher(input).matches()) {
                Matcher m = Pattern.compile("(\\d+)s|(\\d+)b").matcher(input);
                while (m.find()) {
                    if (m.group(1) != null) {
                        strikes = Integer.parseInt(m.group(1));
                    }
                    if (m.group(2) != null) {
                        balls = Integer.parseInt(m.group(2));
                    }
                }
            } else {
                System.out.println("잘못된 입력 형식");
                continue;
            }
            
            Feedback feedback = new Feedback(strikes, balls);
            List<String> filter = new ArrayList<>();
            
            // 피드백을 입력받으면 정확히 일치하는 후보만 남김
            for (String c : candidates) {
                if (getFeedback(c, guess).equals(feedback)) {
                    filter.add(c);
                }
            }
            
            // 필터가 비면 잘못된 것
            if (filter.isEmpty()) {
                System.out.println("후보가 없습니다. 입력값을 확인해 주세요.");
                continue;
            }
            
            // 후보군을 필터로 갱신
            candidates = filter;
            if (candidates.size() == 1) {
            	// 유일한 정답 출력 후 종료
                System.out.println("정답: " + candidates.get(0));
                break;
            }
            
            turn++;
        }

        sc.close();
    }

    // 최적 추측 선택
    private static String chooseBestGuess(List<String> candidates, List<String> allCandidates, Set<String> triedGuesses) {
        int minWorst = Integer.MAX_VALUE;
        String bestGuess = null;

        // 모든 추측을 가지고 후보군과 비교해 가장 피드백이 많이 나오는 그룹 수를 계산한다(최악의 경우)
        for (String guess : allCandidates) {
            // 이미 시도한 추측은 건너뛰기
            if (triedGuesses.contains(guess)) continue;

            Map<Feedback, Integer> map = new HashMap<>();
            for (String actual : candidates) {
                Feedback f = getFeedback(actual, guess);
                map.put(f, map.getOrDefault(f, 0) + 1);
            }
            
            // Knuth 전략의 핵심, 최악의 경우 계산 (가장 많은 후보가 남는 경우)
            int worst = Collections.max(map.values());
            
            // 더 나은 추측을 찾았거나, 동일한 최악의 경우에서 더 작은 숫자를 선택
            if (worst < minWorst || (worst == minWorst && guess.compareTo(bestGuess) < 0)) {
                minWorst = worst;
                bestGuess = guess;
            }
        }

        return bestGuess;
    }
    
    // 피드백
    private static Feedback getFeedback(String answer, String guess) {
        int strikes = 0;
        int balls = 0;
        boolean[] strikeUsed = new boolean[4];
        
        // 스트라이크 계산
        for (int i = 0; i < 4; i++) {
            if (guess.charAt(i) == answer.charAt(i)) {
                strikes++;
                strikeUsed[i] = true;  // 스트라이크 위치 표시
            }
        }
        
        // 볼 계산
        for (int i = 0; i < 4; i++) {
            if (!strikeUsed[i]) {  // 스트라이크가 아닌 위치만 체크
                for (int j = 0; j < 4; j++) {
                    if (i != j && !strikeUsed[j] && guess.charAt(i) == answer.charAt(j)) {
                        balls++;
                        break;  // 한 숫자당 한 번만 볼로 카운트
                    }
                }
            }
        }
        
        // 결과를 객체로 반환
        return new Feedback(strikes, balls);
    }
    
    // 피드백 결과를 구조화해서 비교하고 후보군을 나누기 위해 피드백 클래스 생성
    private static class Feedback {
        int strikes;
        int balls;

        public Feedback(int strikes, int balls) {
            this.strikes = strikes;
            this.balls = balls;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (!(object instanceof Feedback)) return false;
            
            Feedback feedback = (Feedback) object;
            return strikes == feedback.strikes && balls == feedback.balls;
        }

        @Override
        public int hashCode() {
        	// Map에서 올바르게 비교되도록 해시코드 설정
            return Objects.hash(strikes, balls);
        }
    }
}
