def isValid(R):
    count = 0
    idx = 0

    while idx < N:
        count += 1
        # 힘 R로 터질 때 현재 건초더미를 제일 왼쪽으로 두고 2 * R 만큼 오른쪽
        explode = haybales[idx] + (2 * R)

        while idx < N and haybales[idx] <= explode:
            idx += 1
    
    # K마리 이하로 날렸으면 true
    return count <= K

N, K = map(int, input().split())

haybales = [int(input()) for _ in range(N)]
haybales.sort()

left = 0
right = 1000000000
R = 0

while left <= right:
    mid = (left + right) // 2

    if isValid(mid):
        R = mid
        right = mid - 1
    else:
        left = mid + 1

print(R)
