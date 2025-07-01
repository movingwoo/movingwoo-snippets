def dfs(x, y):
    visited[x][y] = True

    if board[x][y] == '-':
        ny = y + 1
        if ny < M and not visited[x][ny] and board[x][ny] == '-':
            dfs(x, ny)
    
    elif board[x][y] == '|':
        nx = x + 1
        if nx < N and not visited[nx][y] and board[nx][y] == '|':
            dfs(nx, y)

N, M = map(int, input().split())

board = []
for i in range(N):
    board.append(list(input().strip()))
        
visited = []
for i in range(N):
    row = []
    for j in range(M):
        row.append(False)
    visited.append(row)

count = 0
for i in range(N):
    for j in range(M):
        if not visited[i][j]:
            dfs(i, j)
            count += 1

print(count)

