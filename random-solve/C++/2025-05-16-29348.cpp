#include <iostream>
#include <vector>
#include <queue>
#include <tuple>
using namespace std;

// 중요한 값 전역변수로
int n, m; // 맵 사이즈
vector<string> map; // 맵
vector<vector<int>> height; // 높이
vector<vector<int>> visited; // 방문했을때 거리
queue<pair<int, int>> que; // BFS용 큐 (x, y 좌표만 저장)

// 방향 배열
int dx[4] = {1, -1, 0, 0}; 
int dy[4] = {0, 0, 1, -1};

// 방문 기록 갱신 함수
void update(int tx, int ty, int cost, int d) {
    if (visited[tx][ty] < 0 || visited[tx][ty] > d + cost) {
        // 첫 방문이거나, 더 짧은 거리로 도착하면 갱신
        visited[tx][ty] = d + cost;
        que.push({tx, ty});
    }
}

int main() {
    int ar, ac, br, bc; // 시작점과 도착점 좌표
    cin >> n >> m;
    cin >> ar >> ac >> br >> bc;
    ar--; ac--; br--; bc--;

    // 맵에 지형 정보 입력
    map.resize(n); 
    for (int i = 0 ; i < n ; i++) {
        cin >> map[i];
    }

    // 높이 정보 입력
    height.assign(n, vector<int>(m));
    for (int i = 0 ; i < n ; i++) {
        for (int j = 0 ; j < m ; j++) {
            cin >> height[i][j];
        }
    }

    // 방문 거리 정보 초기화 (-1은 미방문, 방문 시 거리 저장)
    visited.assign(n, vector<int>(m, -1)); 

    que.push({ar, ac}); // 시작점 삽입
    visited[ar][ac] = 0; // 시작점 거리: 0

    while (!que.empty()) {
        int x, y;
        x = get<0>(que.front());
        y = get<1>(que.front());

        que.pop(); // 현재 위치

        int d = visited[x][y]; // 현재까지 거리

        for (int dir = 0; dir < 4; ++dir) { 
            int nx = x + dx[dir], ny = y + dy[dir]; // 다음 위치

            // 맵 범위 벗어나면 continue
            if (nx < 0 || ny < 0 || nx >= n || ny >= m) continue;

            char now = map[x][y], next = map[nx][ny];
            int nowHeight = height[x][y], nextHeight = height[nx][ny];
            
            if (now == 'B' && (next == 'B' || next == 'S')) {
                // 건물에서 건물 또는 눈으로 이동
                update(nx, ny, 1, d);
            } else if (now == 'S') {
                // 눈에서 이동
                if (next == 'B' || ((next == 'S' || next == 'I') && nextHeight <= nowHeight))
                    // 건물이거나 높이 확인
                    update(nx, ny, 1, d);
            } else if (now == 'I') {
                 // 얼음에서 이동 (건물은 박살나니까 고려 안함)
                if (next == 'S' && nextHeight <= nowHeight){
                    // 눈으로 이동
                    update(nx, ny, 1, d);
                } else if (next == 'I') {
                    // 얼음이면
                    if (nextHeight == nowHeight){
                        // 높이 같으면 이동
                        update(nx, ny, 1, d);
                    } else if (nextHeight < nowHeight) {
                        // 미끄러지기 시작
                        int slide = 1; // 이동 거리
                        bool isBuilding = false; // 건물 충돌 박살 여부
                        int ox = x, oy = y;

                        // 계속 미끄러지기
                        while (true) {
                            if (nx < 0 || ny < 0 || nx >= n || ny >= m) {
                                // 맵 벗어나면 멈춤
                                break; 
                            }
                            if (map[nx][ny] == 'B') { 
                                // 건물 부딪히면 상태 업데이트하고 break
                                isBuilding = true; 
                                break; 
                            } 
                            if (map[nx][ny] != 'I' || height[nx][ny] > height[ox][oy]){
                                // 얼음이 아니고 높이가 높으면 정지
                                break; 
                            } 

                            ox = nx; 
                            oy = ny; 
                            nx += dx[dir]; 
                            ny += dy[dir];

                            slide++;
                        }

                        // 멈췄으면 실제 멈춘 위치로 보정
                        nx -= dx[dir]; 
                        ny -= dy[dir]; 
                        slide--;

                        // 맵 범위, 건물 체크
                        if ((nx < 0 || ny < 0 || nx >= n || ny >= m) || (map[nx][ny] == 'B')) {
                            continue;
                        }

                        // 정상적으로 멈추면 업데이트
                        if (!isBuilding && map[nx][ny] != 'B') {
                            update(nx, ny, slide, d);
                        }
                    }
                }
            }
        }
    }

    // 도착점까지 도달했는지 확인
    if (visited[br][bc] < 0){
        cout << "Impossible";
    } else{
        cout << visited[br][bc];
    }

    return 0;
}
