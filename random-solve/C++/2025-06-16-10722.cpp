#include <iostream>
#include <vector>
#include <tuple>
using namespace std;

// DFS
tuple<double, double, double> DFS(int node, const vector<tuple<int, int, int>> &bar) {

    // 양수면 구슬, 질량만 있음
    if (node > 0) {
        return {static_cast<double>(node), 0.0, 0.0};
    }

    // 음수면 막대
    auto [len, l, r] = bar[-node];


    auto [lm, ll, lr] = DFS(l, bar);
    auto [rm, rl, rr] = DFS(r, bar);

    double tm = lm + rm;

    // 왼쪽 질량 * 왼쪽 거리 == 오른쪽 질량 * 오른쪽 거리
    double ldist = len * (rm / tm);
    double rdist = len - ldist;

    // 서브트리가 반대로 퍼지는 경우를 고려해야함
    double lw = min(-ldist - ll, rdist - rl);
    double rw = max(-ldist + lr, rdist + rr);

    return {tm, -lw, rw};
}

int main() {
    int t;
    cin >> t;

    while (t--) {
        int n;
        cin >> n;
    
        vector<tuple<int, int, int>> bar(n+1);

        for (int i = 1 ; i <= n ; i++) {
            int len, l, r;
            cin >> len >> l >> r;
            bar[i] = make_tuple(len, l, r);
        }

        // 루트 노드는 -1
        auto [mass, left, right] = DFS(-1, bar);

        printf("%.6f\n", left + right);

    }
}
