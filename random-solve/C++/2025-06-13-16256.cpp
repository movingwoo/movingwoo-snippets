#include <iostream>
#include <vector>
using namespace std;

int main() {
    int t;
    cin >> t;

    while (t--) {
        int n, m, k;
        cin >> n >> m >> k;
        
        vector<vector<int>> wall(n, vector<int>(m));
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                cin >> wall[i][j];
            }
        }

        bool paintable = true;

        // 연속된 행 방향 타일 구간 탐색
        for (int i = 0 ; i < n && paintable ; ++i) {
            int cnt = 0;

            for (int j = 0 ; j <= m ; ++j) { 
                if (j < m && wall[i][j] == 1) {
                    cnt++;
                } else {
                    if (cnt > k) {
                        paintable = false;
                        break;
                    }

                    cnt = 0;
                }
            }
        }

        // 연속된 열 방향 타일 구간 탐색
        for (int j = 0 ; j < m && paintable ; ++j) {
            int cnt = 0;

            for (int i = 0 ; i <= n ; ++i) {
                if (i < n && wall[i][j] == 1) {
                    cnt++;
                } else {
                    if (cnt > k) {
                        paintable = false;
                        break;
                    }
                    
                    cnt = 0;
                }
            }
        }

        if (!paintable) {
            cout << "NO\n";
            continue;
        }

        // 가능하면 페인트칠
        vector<vector<int>> painted_wall(n, vector<int>(m, 0));
    
        for (int i = 0; i < n; ++i){
            for (int j = 0; j < m; ++j) {
                if (wall[i][j] == 1) {
                    // 모든 행열 구간에서 중복되지 않게
                    painted_wall[i][j] = ((i + j) % k) + 1;
                }
            }
        }    

        cout << "YES\n";
        for (int i = 0 ; i < n ; ++i) {
            for (int j = 0 ; j < m ; ++j) {
                cout << painted_wall[i][j] << " ";
            }
            cout << "\n";
        }
    }
}
