N = int(input())

pack = list(map(int, input().split()))
# 대충 큰 값
dp = [1000000] * (N + 1)
dp[0] = 0

for i in range(1, N + 1):
    for j in range(1, i + 1):
        dp[i] = min(dp[i], dp[i-j] + pack[j - 1])

print(dp[N])
