#include <iostream>
#include <vector>
#include <tuple>
#include <algorithm>
#include <climits>
using namespace std;

// 커스텀 정렬
bool compare(const tuple<int, int>& a, const tuple<int, int>& b) {
    return get<1>(a) > get<1>(b); 
}

int main() {

	while(true){
		int n;
		cin >> n;

		if (n == 0) break;

		vector<int> x(n);
		vector<int> t(n);
		vector<tuple<int, int>> assignments;

		for (int i = 0 ; i < n ; i++) {
			cin >> x[i];
		}

		for (int i = 0 ; i < n ; i++) {
			cin >> t[i];
		}

		// 튜플로 만들어 넣기
		for (int i = 0; i < n; ++i) {
			assignments.push_back(make_tuple(x[i], t[i]));
		}

		// 늦게 시작하고 싶으니 마감시간이 제일 늦은 과제부터 거꾸로 확인
		sort(assignments.begin(), assignments.end(), compare);

		int time = INT_MAX; 

		// 과제 하나씩 체크
        for (const auto& assignment : assignments) {
			int xi = get<0>(assignment);
			int ti = get<1>(assignment);

			time = min(time, ti);
			time -= xi;
		}

		// 시간이 부족하면 impossible
        if (time < 0) cout << "impossible\n";
        else cout << time << '\n';
	}

	return 0;
}
