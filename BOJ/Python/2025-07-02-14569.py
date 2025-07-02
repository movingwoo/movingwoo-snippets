# 비트마스킹
def masking(n):
    mask = 0
    for i in n:
        mask |= 1 << i

    return mask

N = int(input())
classes = []

for _ in range(N):
   # 첫 숫자 슬라이싱 해야함
   class_time = list(map(int, input().split()))[1:]
   classes.append(masking(class_time))

M = int(input())

for _ in range(M):
    # 첫 숫자 슬라이싱 해야함
    student = masking(list(map(int, input().split()))[1:])
    
    count = 0
    for class_time in classes:
        # 수업 시간과 학생 빈 시간의 교집합
        if class_time & student == class_time:
            count += 1

    print(count)
