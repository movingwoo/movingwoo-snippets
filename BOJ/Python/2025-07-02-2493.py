N = int(input())

towers = list(map(int, input().split()))
stack = [] # 타워 담을 스택
result = [] # 결과 출력용

for i in range(N):
    # 스택 타워가 현재 타워보다 낮으면 수신 못해서 의미 없음
    while stack and stack[-1][1] < towers[i]:
        stack.pop()

    if not stack:
        result.append(0)
    else:
        # 수신 가능함
        result.append(stack[-1][0] + 1)

    stack.append((i, towers[i]))

print(" ".join(map(str, result)))
