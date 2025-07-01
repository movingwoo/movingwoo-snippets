N = int(input())

shuffle = list(map(int, input().split()))
cows = list(map(int, input().split()))

for i in range(N):
    idx = i
    for j in range(3):
        idx = shuffle[idx] - 1
    
    print(cows[idx])
