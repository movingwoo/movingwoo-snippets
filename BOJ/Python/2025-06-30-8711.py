import sys

n = int(sys.stdin.readline())

max, result = 0, 0
w = map(int, sys.stdin.readline().split())

for k in w:
    if max < k:
        max = k
        
    # 지금까지의 최대 무게와 현재 무게의 비교
    if max - k > result:
        result = max - k
        
print(result)
