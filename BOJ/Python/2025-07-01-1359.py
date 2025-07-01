import math

N, M, K = map(int, input().split())

result = 0;

for i in range(K, M + 1):
    result += math.comb(M, i) * math.comb(N - M, M - i)

print(result / math.comb(N, M))
