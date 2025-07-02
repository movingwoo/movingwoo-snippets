N = int(input())
target = int(input())

x, y = N // 2, N // 2

# 달팽이 방향 위 오른쪽 아래 왼쪽
dx = [-1, 0, 1, 0]
dy = [0, 1, 0, -1]

snail = [[0] * N for _ in range(N)]
n = 1
len = 1
tx, ty = 0, 0

snail[x][y] = n

# n == 1 일때 타겟좌표 체크
if n == target:
    tx, ty = x, y

while n < N * N:
    for i in range(4):
        for _ in range(len):
            x += dx[i]
            y += dy[i]

            if 0 <= x < N and 0 <= y < N:
                n += 1
                snail[x][y] = n
                
                if n == target:
                    # 타겟 좌표 미리 저장
                    tx, ty = x, y
        
        if i % 2 == 1:
            # 2번마다 길이 연장
            len += 1

for r in snail:
    print(' '.join(map(str, r)))
print(tx + 1, ty + 1) 
