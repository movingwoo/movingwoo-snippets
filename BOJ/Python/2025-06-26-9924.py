A, B = map(int, input().split())

if (B > A):
    temp = B
    B = A
    A = temp

result = 0

while (True):
    if (A % B == 0):
        result += A // B - 1
        break
    else:
        result += A // B
        temp = A % B
        A = B
        B = temp

print(result)
