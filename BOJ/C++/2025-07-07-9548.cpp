#include <bits/stdc++.h>
#include <ext/rope>
 
using namespace std;
using namespace __gnu_cxx;

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);

    int T;
    cin >> T;
    cin.ignore();

    while (T--) {
        string s;
        getline(cin, s);

        // rope
        rope<char> rope(s.c_str());

        string line;
        while (getline(cin, line)) {
            if (line == "END") break;

            stringstream ss(line);
            string query;
            ss >> query;

            if (query == "I") {
                // insert
                string x;
                int y;
                ss >> x >> y;
                rope.insert(y, x.c_str());
            } else if (query == "P") {
                // print
                int x, y;
                ss >> x >> y;
                for (int i = x; i <= y; ++i) {
                    cout << rope[i];
                }
                cout << '\n';
            }
        }
    }

    return 0;
}
