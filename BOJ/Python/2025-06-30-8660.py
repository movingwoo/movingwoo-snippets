import sys

n = int(sys.stdin.readline())

neg = 0
a = map(int, sys.stdin.readline().split())

for i in a:
    if i < 0:
        neg += 1

# a != b
print(neg * (n - 1))
