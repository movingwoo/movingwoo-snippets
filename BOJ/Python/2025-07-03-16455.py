def kth(a, k):
    import random

    n = len(a)
    start = 0
    end = n - 1
    k -= 1

    while start <= end: 
        # 3분할
        i = start
        j = start
        l = end

        # 무작위 값
        pivot = a[random.randint(i, l)]

        while i <= l:
            # pivot보다 작을때
            if a[i] < pivot:
                a[i], a[j] = a[j], a[i]
                i += 1
                j += 1
            # pivot보다 클떄
            elif a[i] > pivot:
                a[i], a[l] = a[l], a[i]
                l -= 1
            else:
                i += 1
        
        
        if k < j:
            end = j - 1
        elif k > l:
            start = l + 1
        else:
            return pivot
