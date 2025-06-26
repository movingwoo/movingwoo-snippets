T = int(input())

for t in range(T):

    N = int(input())

    max_x, max_y = -1001.0, -1001.0
    min_x, min_y = 1001.0, 1001.0

    for _ in range(N):
        x, y = map(float, input().split())
        max_x = max(max_x, x)
        min_x = min(min_x, x)
        max_y = max(max_y, y)
        min_y = min(min_y, y)
    
    x = max_x - min_x
    y = max_y - min_y

    print(f"Case {t+1}: Area {x * y :.9f}, Perimeter {2 * (x + y) :.9f}")
    
