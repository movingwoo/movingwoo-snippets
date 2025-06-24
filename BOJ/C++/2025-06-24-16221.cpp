#include <iostream>
using namespace std;

// 하수인
int minions_bit[1000001];
// 전장에 없는 체력
int absent_bit[1000001];
int check[1000001];

// BIT에 값 추가, 제거
void update(int bit[], int i, int n) {
    while (i < 1000001) {
        bit[i] += n;
        i += i & -i;
    }
}

// 1~i까지 합
int bit_sum(int bit[], int i) {
    int result = 0;
    while (i > 0) {
        result += bit[i];
        i -= i & -i;
    }
    return result;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    // 초기화
    for (int i = 1; i < 1000001; i++) {
        update(absent_bit, i, 1);
    }

    int Q;
    cin >> Q;

    while (Q-- > 0) {
        int T, K;
        cin >> T >> K;
        
        // 1이면 하수인 추가 2면 제거
        if (T == 1) {
            if (check[K]++ == 0) {
                // 하수인 처음 추가될때만 제거
                update(absent_bit, K, -1);
            }
            update(minions_bit, K, 1);
        } else {
            update(minions_bit, K, -1);

            if (--check[K] == 0) {
                // 하수인 마지막 제거될때만 추가
                update(absent_bit, K, 1);
            }
            
        }

        // 이진 탐색
        int idx = 0;
        int bitMask = 1 << 20;
        int sum = 0;
        
        while (bitMask) {
            int t = idx + bitMask;
            
            // 없는 체력이 있으면
            if (t < 1000001 && sum + absent_bit[t] < 1) {
                sum += absent_bit[t];
                idx = t;
            }
            bitMask >>= 1;
        }
        // 실제 체력 보정
        ++idx;

        cout << bit_sum(minions_bit, idx - 1) << '\n';
    }
    
    return 0;
}
