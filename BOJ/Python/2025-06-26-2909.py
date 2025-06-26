from decimal import Decimal, ROUND_HALF_UP

C, K = map(int, input().split())

shift = Decimal('1e{}'.format(K))
result = (Decimal(C) / shift).to_integral_value(rounding=ROUND_HALF_UP) * shift

print(int(result))
