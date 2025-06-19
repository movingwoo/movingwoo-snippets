#include <iostream>
using namespace std;
using ull = unsigned long long;

// 최대 비트수는 60이며 최대 점수는 모두가 1인 경우 60^2
static const int MAX_BITS = 60;
static const int MAX_SCORE = 60 * 60;

ull find_score(ull dp[MAX_BITS + 1][MAX_SCORE + 1], ull target, int c_score) {

    // 60^2를 넘을 경우 예외처리
    if (c_score > MAX_SCORE) {
        return 0;
    }

    ull count = 0;
    int score = 0, len = 0;

    // 최상위 비트부터
    for(int i = MAX_BITS - 1 ; i >= 0 ; --i) {
        bool bit = (target >> i) & 1;

        // 1이면 0으로 바꿔서 더 작은 수 계산
        // 0이면 점수추가
        if (bit) {

            int need_score = c_score - (score + len * len);
            if (need_score < 0) {
                need_score = 0;
            }

            // i비트로 필요 점수 이상 만들 수 있는 경우의 수
            count += dp[i][need_score];
            ++len;
        } else {
            if (len > 0) {
                score += len * len;
                len = 0;
            }
        }
    }
    // 1이 남은 경우 점수 추가
    if (len > 0) score += len * len;
    
    // 전달받은 a - 1과 b도 포함
    if (score >= c_score) {
        ++count;
    }
    
    return count;
}

int main(){
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    // 전처리 시작, 가능한 모든 패턴을 미리 계산
    ull dp[MAX_BITS + 1][MAX_SCORE + 1];
    // dp[i][j] = 0 ≤ n < 2^i 중에서 f(n) ≥ j인 개수, f(n)은 n의 이진 표현에서 연속된 1 구간들의 길이 제곱 합
    // 점화식: dp[i][j] = dp[i - 1][j] + Σ(k = 1 to i) dp[i - k - 1][max(0, j - k^2)]

    // 점수 0은 0비트에서만 가능하므로 1개
    dp[0][0] = 1;

    for(int j = 1 ; j <= MAX_SCORE ; ++j) {
        // 0비트에서는 0보다 큰 점수가 존재하지 않음
        dp[0][j] = 0;
    }

    // i비트 수까지 확장
    for(int i = 1 ; i <= MAX_BITS ; ++i) {
        for(int j = 0 ; j <= MAX_SCORE ; ++j) {
            
            // 1. 최상위 비트를 0으로 두는 경우 (0xxx 형태)
            // 연속 1구간이 아니므로 i-1 비트로 j 이상 만드는 경우의 수
            dp[i][j] = dp[i - 1][j];

            // 2. 최상위 비트에서 연속 k개의 1을 쓰는 경우 (110xxx 형태)
            // 그 다음 비트가 있다면 0으로 강제, 길이 k의 연속 1 구간이 생성되고, 그 다음부터는 새로운 구간
            for(int k = 1 ; k <= i ; ++k) {
                // k개의 1 다음에 남는 비트 수
                int zero_bit = i - k - 1;          
                if (zero_bit < 0) {
                    // k == i일 때 모든 비트가 1
                    zero_bit = 0;
                }

                // 앞 연속구간 점수 k^2를 제외하고 남은 필요 점수
                int need_score = j - k * k;                
                if (need_score < 0) {
                    need_score = 0;
                }

                // 남은 비트들로 필요 점수 이상 만들 수 있는 경우의 수
                dp[i][j] += dp[zero_bit][need_score];         
            }
        }
    }
    // 전처리 종료

    int T;
    cin >> T;
    while (T--) {
        ull a, b, c;
        cin >> a >> b >> c;

       
        int c_score = 0, len = 0;

         // c의 점수 계산 (연속된 1 구간 길이의 제곱 함)
        for(int i = 0 ; i < MAX_BITS ; ++i) {
            // 1이면 길이증가, 0이면 점수추가
            if (c & (1ULL << i)) {
                ++len;  
            } else if (len > 0) {
                c_score += len * len;
                len = 0;
            }
        }
        // 1이 남은 경우 점수 추가
        if (len > 0) {
            c_score += len * len;
        }
        
        // 0 ~ b 구간 점수와 0 ~ a - 1 구간 점수의 차
        ull sector_b = find_score(dp, b, c_score + 1);
        // a가 0이면 0 처리
        ull sector_a = a == 0 ? 0 : find_score(dp, a - 1, c_score + 1);
        
        cout << (sector_b - sector_a + 1) << "\n";
    }

    return 0;
}
