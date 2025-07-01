T = int(input())

for _ in range(T):
    N, M = map(int, input().split())

    X = int(''.join(input().split()))
    Y = int(''.join(input().split()))

    wheel = list(map(int, input().split()))

    count = 0

    for i in range(N):
        idx = i
        num = 0

        for j in range(M):
            num *= 10
            num += wheel[idx]
            idx = (idx + 1) % N

        if X <= num <= Y:
            count += 1

    print(count)
