T = int(input())

for t in range(T):
    if (t != 0):
        print()

    string_num = input().strip()
    num = int(string_num)

    print(num)

    while num > 99:
        remain = num % 10
        num = num // 10 - remain
        print(num)
        
    if (num % 11 == 0):
        print("The number {} is divisible by 11.".format(string_num))
    else:
        print("The number {} is not divisible by 11.".format(string_num))
