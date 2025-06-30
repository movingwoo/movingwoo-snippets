from collections import defaultdict

T = int(input())

for _ in range(T):
    
    sascha_word = input().strip()
    w = int(input().strip())

    dictionary = defaultdict(str)

    for _ in range(w):
        word = input().strip()
        count = 0
        for sascha_char, word_char in zip(sascha_word, word):
            if sascha_char != word_char:
                count += 1
        
        # 최초 사전 입력 유지
        if count not in dictionary:
            dictionary[count] = word
        
    print(dictionary[min(dictionary.keys())])

