#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <cctype>
using namespace std;

// 소문자 변환
string toLower(const string& s) {
    string lower;
    lower.reserve(s.size());

    for (char c : s) {
        lower += tolower(c);
    }

    return lower;
}

// 대소문자 차이 계산
int caseDifference(const string& a, const string& b, int K) {
    int cnt = 0;

    for (int i = 0; i < a.size(); ++i) {
        if (a[i] != b[i] && tolower(a[i]) == tolower(b[i])) {
            cnt++;

            // K 초과 종료
            if (cnt > K) {
                return cnt; 
            }
        }
    }

    return cnt;
}

int main() {
    // 줄여야 하는 것
    // 1. 문자열 비교 대상 (전체 비교하면 터짐) -> 소문자 별도 저장
    // 2. 소문자 전환 (tolower를 너무 자주 쓰면 시간 터짐) -> 마스킹 처리 후 비트 기반 XOR 연산
    // 이래도 시간초과???
    // 3. K 이하 위치만 토글해서 가능한 모든 마스크 조합을 만들기? -> K가 최대 5이므로 시도할만함.. 메모리가 버틸지?
    // 더 멀리 못가고 시간초과... 하나만 더 해보고 시간초과나면 새로운 방식으로 시도
    // 4. 문자열 마스크 -> 비트 마스크, 해시 테이블 탐색만으로 비교
    // 때려치운다. 이진탐색으로 바꾸자.
    // 5. 소문자로 정렬하고 이진탐색 
    // 또 실패 일단 초심으로 돌아가서 다시 생각
    // 6. 후보 집합 필터링 후 해시 기반 탐색

    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int K, W, Q;
    cin >> K >> W;

    // 소문자 + 길이 기준
    unordered_map<string, unordered_map<int, vector<string>>> words;

    for (int i = 0; i < W; ++i) {
        string word;
        cin >> word;

        string lower = toLower(word);
        int length = word.size();

        words[lower][length].push_back(word);
    }

    cin >> Q;

    for (int i = 0; i < Q; ++i) {
        string query;
        cin >> query;

        string lowerQuery = toLower(query);
        int length = query.size();
        int result = 0;

        // 소문자 + 길이 일치하는 단어만 대상으로 필터링
        if (words.count(lowerQuery) && words[lowerQuery].count(length)) {

            const vector<string>& list = words[lowerQuery][length];

            for (const string& target : list) {
                int cnt = caseDifference(query, target, K);
                if (cnt <= K) {
                    result++;
                }
            }
        }

        cout << result << '\n';
    }

    return 0;
}
