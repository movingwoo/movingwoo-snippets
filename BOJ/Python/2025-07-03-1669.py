X, Y = map(int, input().split())
sub = Y - X
k = 0
result = 0

while sub > 0:

    if k * k >= sub:
        result = 2 * k - 1
        break

    elif k * (k + 1) >= sub:
        result = 2 * k
        break

    k += 1

print(result)
