str_n, o = input().split()
n = int(str_n)

deck = list(range(n))

# 아웃셔플
def out_shuffle(deck):
    # 왼쪽이 더 많게
    mid = (n + 1) // 2  
    left = deck[:mid]
    right = deck[mid:]

    result = []
    for i in range(n):
        if i % 2 == 0:
            result.append(left[i // 2])
        else:
            result.append(right[i // 2])
    return result

# 인셔플
def in_shuffle(deck):
    # 오른쪽이 더 많게
    mid = n // 2
    left = deck[:mid]
    right = deck[mid:]
    
    result = []
    for i in range(n):
        if i % 2 == 0:
            result.append(right[i // 2])
        else:
            result.append(left[i // 2])
    return result

shuffled_deck = deck
cnt = 0

while True:
    if o == 'in':
        shuffled_deck = in_shuffle(shuffled_deck)
    elif o == 'out':
        shuffled_deck = out_shuffle(shuffled_deck)
    
    cnt += 1

    if shuffled_deck == deck:
        break

print(cnt)
