def solve():
    score = int(input())

    # 무빙
    moving_input = input()
    moving = [moving_input[i:i+4] for i in range(0, len(moving_input), 4)]

    # 현재 판
    board_input = list(map(int, input().split()))
    board = [board_input[i*4:(i+1)*4] for i in range(4)]

    # 무빙만큼 반복
    for move in moving:
        dir = move[0]
        value = int(move[1])
        x = int(move[2])
        y = int(move[3])

        board, gained = move_board(board, dir)
        score += gained

        board[x][y] = value

    print(score)

def move_board(board, dir):
    gained = 0

    # 방향에 따라 움직일 방향 설정
    dir_delta = {
        'U': (0, -1),
        'D': (0, 1),
        'L': (-1, 0),
        'R': (1, 0),
    }

    dx, dy = dir_delta[dir]

    # 병합 여부 확인
    merged = [[False]*4 for _ in range(4)]

    # 벽은 무시하고 탐색해야해서 이동 순서 조정
    range_x = range(4)
    range_y = range(4)
    if dir == 'R':
        range_x = range(2, -1, -1)
    if dir == 'D':
        range_y = range(2, -1, -1)
    if dir == 'L':
        range_x = range(1, 4)
    if dir == 'U':
        range_y = range(1, 4)

    # 설정 범위내 무빙
    for y in range_y:
        for x in range_x:
            # 0이면 무시
            if board[y][x] == 0:
                continue

            # 현재 좌표
            cx, cy = x, y
            while True:
                # 목표 좌표
                nx, ny = cx + dx, cy + dy

                # 벽 만나면 중단
                if not (0 <= nx < 4 and 0 <= ny < 4):
                    break

                # 빈 칸이면 이동
                if board[ny][nx] == 0:
                    board[ny][nx] = board[cy][cx]
                    board[cy][cx] = 0
                    cx, cy = nx, ny

                # 같은 숫자 + 병합 안 됐으면 합치기
                elif board[ny][nx] == board[cy][cx] and not merged[ny][nx]:
                    board[ny][nx] *= 2
                    board[cy][cx] = 0
                    merged[ny][nx] = True
                    gained += board[ny][nx]
                    break
                # 다른 숫자면 무시
                else:
                    break
    
    return board, gained

if __name__ == "__main__":
    solve()
