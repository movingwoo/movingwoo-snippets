class SegmentTree:
    def __init__(self, n):
        self.n = n
        self.size = 1 

        while self.size < self.n:
            self.size *= 2

        self.tree = [0] * (2 * self.size)

    def update(self, idx, val):
        idx += self.size
        # 갱신이 아니라 합치기
        self.tree[idx] += val

        while idx > 1:
            idx //= 2 
            self.tree[idx] = self.tree[idx * 2] + self.tree[idx * 2 + 1]
            
    def query(self, l, r):
        l += self.size
        r += self.size
        result = 0

        while l <= r:
            if l % 2 == 1:
                result += self.tree[l]
                l += 1
            if r % 2 == 0:
                result += self.tree[r]
                r -= 1

            l //= 2
            r //= 2

        return result


N, Q = map(int, input().split())
tree = SegmentTree(N)

for _ in range(Q):
    q, p, x = map(int, input().split())

    # 1은 업데이트 2는 구간합
    if q == 1:
        tree.update(p - 1, x)
    elif q == 2:
        print(tree.query(p - 1, x - 1))
