import re

while True:
    haikus = input().strip()
    if haikus == 'e/o/i':
        break

    haiku = haikus.split('/')
    jo = [5, 7, 5]
    result = 1;
    
    for i in range(3):

        if len(re.findall(r'[aeiouy]+', haiku[i])) != jo[i]:
            print(result)
            break

        result += 1
    
    if result == 4:
        print('Y')
