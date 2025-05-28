#include <iostream>
#include <cmath>
#include <cstdio>
using namespace std;

int main() {

    int n;
    cin >> n;

    while (n--) {
        double T, W, C;
        cin >> T >> W >> C;
        double result;


        if (C <= W) {
            // 도보가 빠르면 원 면적 계산
            // (W*T)^2 * π
            result = W * W * T * T * M_PI;
        } else {
            // 차량이 빠르면 공식에 대입
            // 2 * T^2 * (W^2 * arcsin(W/C) + W * sqrt(C^2 - W^2))
            double term1 = W * W * asin(W / C); // W^2 * arcsin(W/C)
            double term2 = W * sqrt(C * C - W * W); // W * sqrt(C^2 - W^2)
            result = 2.0 * T * T * (term1 + term2);
        }

        // 소수점 셋째 자리까지 출력
        printf("%.3f\n", result);
    }
    
    return 0;
}
