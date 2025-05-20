import java.io.*;
import java.util.*;

public class Main {
    public static int[] factorial, invFactorial;

    public static void main(String[] args) {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out))) {
            
            StringTokenizer st = new StringTokenizer(br.readLine(), " ");
            int N = Integer.parseInt(st.nextToken());
            int M = Integer.parseInt(st.nextToken());
            int K = Integer.parseInt(st.nextToken());

            StringBuilder sb = new StringBuilder();
            
            // a번: 이진수 M의 1을 반전시킨 수 중 최대값
            // 최대값 저장용
            int a = Integer.MIN_VALUE;
            // 0번째 비트부터 올라감, 100,000,000는 27비트
            for (int i = 0 ; i < 27 ; i++){
                // M의 i번째 비트가 1인 경우
                // == 1 할 경우 0번째 비트만 체크해서 오류 발생
                //if ((M & (1 << i)) == 1) {
                if ((M & (1 << i)) != 0) {
                    int m = M ^ (1 << i); // 반전
                    
                    if (m < M && m > a) {
                        a = m;
                    }
                }
            }

            sb.append(a).append("\n");

            // b번: M의 비트 하나를 반전시킨 수 중 M보다 큰 최소값 찾기
            // 최소값 저장용
            int b = Integer.MAX_VALUE;

            for (int i = 0 ; i < 27 ; i++){
                // i번째 비트를 반전시킨 숫자
                int m = M ^ (1 << i);
                if (m > M && b > m) {
                    b = m;
                }
            }

            sb.append(b).append("\n");

            // c번: 공식계산
            doFactorials(N);

            // 초기 상태 dp0: 해밍 무게 w를 가진 노드 수 = C(N, w)
            int[] dp = new int[N + 1];
            for (int w = 0; w <= N; w++) {
                dp[w] = C(N, w);
            }

            // 전이 행렬 생성 (희소 형태로)
            List<List<Pair>> T = new ArrayList<>();
            for (int i = 0; i <= N; i++) {
                T.add(new ArrayList<>());
            }

            for (int w = 0; w < N; w++) {
                // 해밍 무게가 w인 노드에서 w+1로 갈 수 있는 경우만 존재
                T.get(w + 1).add(new Pair(w, N - w));
            }

            // 희소 행렬 거듭제곱을 dp에 적용
            int[] dpK = sparseMatPowApply(T, dp, K, N + 1);

            // 모든 해밍 무게에서 끝나는 경로 수 합
            int c = 0;
            for (int w = 0; w <= N; w++) {
                c = (c + dpK[w]) % 100003;
            }

            sb.append(c);
            bw.write(sb.toString());

        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    // 거듭제곱 계산
    public static int P(int b, int e) {
        int r = 1;
        while (e > 0) {
            if ((e & 1) == 1) {
                // 거듭제곱 계산
                r = (int)((1L * r * b) % 100003);
            }
            b = (int)((1L * b * b) % 100003);
            e >>= 1;
        }
        return r;
    }

    // 팩토리얼 및 역팩토리얼 계산 (페르마 소정리 활용)
    public static void doFactorials(int N) {
        factorial = new int[N + 1];
        invFactorial = new int[N + 1];
        factorial[0] = 1;

        for (int i = 1; i <= N; i++) {
            // 팩토리얼 계산
            factorial[i] = (int)((1L * factorial[i - 1] * i) % 100003);
        }

        // 역팩토리얼 (페르마의 소정리)
        invFactorial[N] = P(factorial[N], 100003 - 2);
        for (int i = N - 1; i >= 0; i--) {
            // 역팩토리얼 누적 계산
            invFactorial[i] = (int)((1L * invFactorial[i + 1] * (i + 1)) % 100003);
        }
    }

    // 이항계수 계산
    public static int C(int N, int w) {
        if (w < 0 || w > N) {
            return 0;
        }
        // C(n, r) 계산
        return (int)(1L * factorial[N] * invFactorial[w] % 100003 * invFactorial[N - w] % 100003);
    }

    // 희소행렬 형태: 각 행마다 (열, 값) 쌍 저장
    public static class Pair {
        int col, val;
        Pair(int col, int val) {
            this.col = col;
            this.val = val;
        }
    }

    // 희소 행렬 곱셈
    public static int[] sparseMatVecMul(List<List<Pair>> mat, int[] vec, int size) {
        int[] res = new int[size];
        for (int i = 0; i < size; i++) {
            for (Pair p : mat.get(i)) {
                res[i] = (int)((res[i] + 1L * p.val * vec[p.col]) % 100003);
            }
        }
        return res;
    }

    // 희소 행렬 거듭제곱과 곱셈
    public static int[] sparseMatPowApply(List<List<Pair>> T, int[] vec, int exp, int size) {
        // 초기 단위 행렬 적용: vec는 dp로 시작
        while (exp > 0) {
            if ((exp & 1) == 1) {
                vec = sparseMatVecMul(T, vec, size);
            }

            // T = T * T (희소 곱)
            List<List<Pair>> newT = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                newT.add(new ArrayList<>());
            }

            for (int i = 0; i < size; i++) {
                for (Pair kv1 : T.get(i)) {
                    for (Pair kv2 : T.get(kv1.col)) {
                        int val = (int)((1L * kv1.val * kv2.val) % 100003);
                        newT.get(i).add(new Pair(kv2.col, val));
                    }
                }
            }

            // 중복 열 통합
            for (int i = 0; i < size; i++) {
                if (newT.get(i).isEmpty()) continue;
                List<Pair> row = newT.get(i);
                row.sort(Comparator.comparingInt(p -> p.col));

                List<Pair> compressed = new ArrayList<>();
                int lastCol = -1, sum = 0;
                for (Pair p : row) {
                    if (p.col == lastCol) {
                        sum = (sum + p.val) % 100003;
                    } else {
                        if (lastCol != -1) compressed.add(new Pair(lastCol, sum));
                        lastCol = p.col;
                        sum = p.val;
                    }
                }
                if (lastCol != -1) compressed.add(new Pair(lastCol, sum));
                newT.set(i, compressed);
            }

            T = newT;
            exp >>= 1;
        }

        return vec;
    }
}
