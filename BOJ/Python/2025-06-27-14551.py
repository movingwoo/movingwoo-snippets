N, M = map(int, input().split())

result = 1;

while N > 0:
    A = int(input())

    if A != 0:
        result *= A
        result %= M

    N -= 1

print(result % M)
