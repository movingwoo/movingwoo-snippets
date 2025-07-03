from collections import deque

N, M = map(int, input().split())

# 그래프, 간선, 진입차수, 큐
graph = [[] for _ in range(N + 1)]
edges = [tuple(map(int, input().split())) for _ in range(M)]
degree = [0] * (N + 1)
que = deque()
result = []

for A, B in edges:
    graph[A].append(B)
    degree[B] += 1

# 진입차수 0
for i in range(1, N + 1):
    if degree[i] == 0:
        que.append(i)

while que:
    now = que.popleft()
    result.append(now)

    # 연결된 노드 진입차수 감소
    for node in graph[now]:
        degree[node] -= 1
        if degree[node] == 0:
            que.append(node)

print(" ".join(map(str, result)))
