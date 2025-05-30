N, M = map(int, input().split())

numbers = list(map(int, input().split()))
# 오름차순
numbers.sort()

result = M * [0]
visited = N * [False]

def back_tracking(depth):
    if depth == M:
        # M개 까지 출력
        print(*result, sep=' ')
        return
    
    for i in range(N):
        if not visited[i]:
            # 방문 처리
            visited[i] = True
            result[depth] = numbers[i]
            back_tracking(depth + 1)

            visited[i] = False

# 재귀
back_tracking(0)
