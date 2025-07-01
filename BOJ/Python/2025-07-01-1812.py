N = int(input())

solve = 0
candies = []

for i in range(N):
    candy = int(input())
    candies.append(candy)
    if i % 2 == 0:
        solve += candy
    else:
        solve -= candy

solve //= 2
print(solve)

for candy in candies[:-1]:
    candy2 = candy - solve
    print(candy2)
    solve = candy2
