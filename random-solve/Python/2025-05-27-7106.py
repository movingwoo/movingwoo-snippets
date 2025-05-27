K5 = input().split()

r = 0

# 적절한 숫자 생성(중복제외)
all_num = set()

# 몇개 안되고 귀찮으니 5중반복문
for i in range(5):
    for j in range(5):
        if j == i:
            continue
        for k in range(5):
            if k == i or k == j:
                continue
            for l in range(5):
                if l == i or l == j or l == k:
                    continue
                for m in range(5):
                    if m == i or m == j or m == k or m == l:
                        continue
                    num_str = K5[i] + K5[j] + K5[k] + K5[l] + K5[m]
                    if num_str[0] != '0':
                        all_num.add(int(num_str))

all_num = list(all_num)
all_num.sort()
length = len(all_num)
max = all_num[length-1]

# list 순회하며 s1 + s2 + s3 = s4 조건 확인
if length >= 4:

    for i in range(length):
        for j in range(i + 1, length):
            for k in range(j + 1, length):
                s1, s2, s3 = all_num[i], all_num[j], all_num[k]
                s4 = s1 + s2 + s3

                if s4 > max:
                    # 최대 숫자보다 크면 탈출
                    break
                if s4 in all_num:
                    r+=1

print(r)

