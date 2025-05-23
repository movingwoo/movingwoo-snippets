#include <iostream>
#include <cstdint>
#include <bitset>
using namespace std;

// 가설 1: n == 2^k + 1 이면 반드시 줄리아는 1번 자리에 앉으면 됨
bool hypothesis_1(int64_t n) {
    return (n > 1) && ((n - 1) & (n - 2)) == 0;
}

// 가설 2: n == 2^a + 2^b + 1 형태인지 확인
// n - 1 == 2^a + 2^b 라면 가능성 있음
bool hypothesis_2(int64_t n) {
    int64_t x = n - 1;
    int count = 0;
    while (x > 0) {
        if (x & 1) count++;
        if (count > 2) return false;
        x >>= 1;
    }
    return count == 2;
}


// 균등분할이 가능할지
bool can_seat(int64_t len) {
    if (len < 2) return true;
    if (len % 2 == 0) return false;
    return can_seat((len - 1) / 2);
}

int64_t solve(int64_t n) {
    // 짝수일 경우 패스
    if (n % 2 == 0) {
        return -1;
    }

    // 가설 1
    if (hypothesis_1(n)) {
        return 1;
    }

    // 가설 2
    if (!hypothesis_2(n)) {
        return -1;
    }

    // 가설 3
    for (int i = 0 ; i < 64 ; i++){
        int64_t pos = (1LL << i) + 1;
        if (pos > n) {
            break;
        }
        if (can_seat(pos - 2) && can_seat(n - pos - 1)) {
            return pos;
        }
    }

    return -1;
}

int main() {
    // 가설 1: n =2^k + 1 (k > 0) 인 경우 답은 반드시 1이다.  
    // 가설 2: n != 2^a + 2^b + 1 인 경우 배치가 불가능하다.  
    // 가설 3: 그 외의 경우는 시뮬레이션을 하되, 2^k + 1 인 자리만 대상으로 한다. 

    int64_t n;
    cin >> n;

    // n이 1이면 바로 1 출력
    if (n == 1) { 
        cout << 1; 
    } else {
        int64_t result = solve(n);

        if (result == -1) {
            cout << "impossible";
        } else {
            cout << result;
        }
    }

    return 0;
}
