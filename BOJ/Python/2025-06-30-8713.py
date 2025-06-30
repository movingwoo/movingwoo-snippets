a, b = map(int, input().split())

sum = a + b
sub = a - b
mul = a * b

max = max(sum, sub, mul)
count = [sum, sub, mul].count(max)

if count > 1:
    print("NIE")
else:
    print(
        f"{'(' if a < 0 else ''}{a}{')' if a < 0 else ''}"
        f"{'+' if max == sum else '-' if max == sub else '*'}"
        f"{'(' if b < 0 else ''}{b}{')' if b < 0 else ''}"
        f"{'=(' if max < 0 else '='}{max}{')' if max < 0 else ''}"
    )
