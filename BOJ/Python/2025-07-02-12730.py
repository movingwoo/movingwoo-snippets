N = int(input())

for t in range(1, N + 1):
    S = int(input())
    engines = [input().strip() for _ in range(S)]

    Q = int(input())
    queries = [input().strip() for _ in range(Q)]

    # 사용한 검색엔진
    used_engines = set()
    switch = 0

    for query in queries:
        if query not in used_engines:
            used_engines.add(query)

            # 사용한 검색엔진이 가득참 == 스위치 해야함
            if len(used_engines) == S:
                switch += 1
                # 초기화
                used_engines = {query}
                
    print(f"Case #{t}: {switch}")
