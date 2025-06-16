t = int(input())

for _ in range(t):
    n = int(input())
    people = list(input().strip())
    
    count = {}
    last_index = {}
    
    # 각 그룹 별 인원 수 카운팅
    for i in range(n):

        if people[i] not in count:
            count[people[i]] = 1
        else:
            count[people[i]] += 1
        
        # 그룹의 마지막 인덱스
        last_index[people[i]] = i

    
    groups = list(last_index.items())

    # 그룹의 마지막 인덱스 기준으로 선택 정렬 
    for i in range(len(groups)):
        idx = i
        for j in range(i + 1, len(groups)):
            if groups[idx][1] > groups[j][1]:
                idx = j

        groups[i], groups[idx] = groups[idx], groups[i]


    result = 0
    idx = 0

    # 실제 절약된 시간 = (저장해둔 마지막 인덱스 - 현재 마지막 인덱스) * 5 * 인원 수
    for group, index in groups:
        group_count = count[group]
        last_group_idx = idx + group_count - 1

        time = (index - last_group_idx) * 5 * group_count

        result += time
        idx += group_count

    print(result)
