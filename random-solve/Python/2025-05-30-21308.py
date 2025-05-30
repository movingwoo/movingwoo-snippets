import sys

isError = False
# 입력 버퍼 탐색 인덱스
buffer_index = 0
# 프로그램 탐색 인덱스
program_index = 0
# 명령어 사전
opcodes = [
    # 4자리 명령부터 판단
    # CODE | PARAM | DESCRIPTION
    ("1000", None, "ADD"),
    ("1001", None, "SUB"),
    ("1002", None, "MUL"),
    ("1010", None, "DIV"),
    ("1011", None, "MOD"),
    ("1200", None, "PRINT_CHAR"),
    ("1201", None, "PRINT_NUM"),
    ("1210", None, "READ_CHAR"),
    ("1211", None, "READ_NUM"),
    ("000", "number", "PUSH_POS"),
    ("001", "number", "PUSH_NEG"),
    ("020", None, "DUP"),
    ("021", None, "SWAP"),
    ("022", None, "DROP"),
    ("110", None, "STORE"),
    ("111", None, "LOAD"),
    ("200", "label", "LABEL"),
    ("201", "label", "CALL"),
    ("202", "label", "JUMP"),
    ("210", "label", "JUMP_IF_ZERO"),
    ("211", "label", "JUMP_IF_NEG"),
    ("212", None, "RETURN"),
    ("222", None, "HALT"),
]
stack = []
heap = {}
# 복귀 주소 스택
call_stack = []
# CALL, JUMP 등을 처리하기 위해 label 위치를 미리 매핑해야함
labels = {}
# 출력은 리스트에 모아두었다가 한 번에 출력
output = []

# 버퍼에서 숫자 하나 읽어오는 함수
def read_number():
    global buffer_index, isError

    # 입력에 공백이 주어지나??
    while buffer_index < len(input_buffer) and input_buffer[buffer_index].isspace():
        buffer_index += 1

    if buffer_index >= len(input_buffer):
        isError = True
        return 0
    
    start = buffer_index
    # 입력 값이 음수일 수 있음
    if input_buffer[buffer_index] == '-':
        buffer_index += 1

    while buffer_index < len(input_buffer) and input_buffer[buffer_index].isdigit():
        buffer_index += 1

    if start == buffer_index or (input_buffer[start] == '-' and buffer_index == start + 1):
        isError = True
        return 0

    return int(''.join(input_buffer[start:buffer_index]))

# 버퍼에서 문자 하나 읽어오는 함수
def read_char():
    global buffer_index, isError

    if buffer_index >= len(input_buffer):
        isError = True
        return 0
    
    ch = input_buffer[buffer_index]
    buffer_index += 1
    return ord(ch)

# 프로그램에서 2로 끝나는 레이블을 읽어오는 함수
def read_label():
    global program_index, isError
    start = program_index

    while program_index < len(program):
        trit = program[program_index]

        if trit == '2':
            label = program[start:program_index]
            program_index += 1
            return label
        
        elif trit not in '01':
            isError = True
            return None
        
        program_index += 1
    
    isError = True
    return None

# 프로그램 내의 숫자 파싱하는 함수
def parse_number_program():
    global program_index, isError
    bits = []
    count = 0

    while program_index < len(program):
        trit = program[program_index]
        if trit == '0' or trit == '1':
            bits.append(trit)
            program_index += 1
            count += 1

            if count > 31:
                isError = True
                return 0
            
        elif trit == '2':
            program_index += 1

            if not bits:
                isError = True
                return 0
            
            return int(''.join(bits), 2)
        else:
            isError = True
            return 0

    isError = True
    return 0

# label 스캔하는 함수
def scan_labels():
    global labels, isError
    scan_index = 0


    while scan_index < len(program):

        # 트릿이 아니면 스캔 종료
        if program[scan_index] not in '012':
            break
            
        matched = False

        for code, param, description in opcodes:
            if program.startswith(code, scan_index):
                scan_index += len(code)
                matched = True

                # HALT 이후까지 스캔하면 불필요한 오류 발생 가능
                if description == "HALT":
                    # return 해버리니 뒤쪽 레이블이 등록안됨...
                    break
                
                elif description == "LABEL":
                    # label 읽기
                    start = scan_index

                    while scan_index < len(program):
                        trit = program[scan_index]

                        if trit == '2':
                            label = program[start:scan_index]
                            scan_index += 1

                            if not label or any(c not in '01' for c in label):
                                isError = True
                                return
                            
                            if label in labels:
                                # 같은 위치에 중복된 레이블이면 허용
                                if labels[label] != scan_index:
                                    isError = True
                                    return
                            else:
                                # label 다음 위치
                                labels[label] = scan_index
                            break

                        elif trit not in '01':
                            isError = True
                            return
                        scan_index += 1
                    else:
                        # 끝까지 2를 못 만남
                        isError = True
                        return

                # 숫자 건너뛰기
                elif param == "number":
                    count = 0

                    while scan_index < len(program):
                        trit = program[scan_index]
                        scan_index += 1

                        if trit == '2':
                            break
                        elif trit not in '01':
                            isError = True
                            return
                        count += 1

                        if count > 31:
                            isError = True
                            return

                # 레이블 건너뛰기
                elif param == "label":
                    
                    while scan_index < len(program):
                        trit = program[scan_index]
                        scan_index += 1

                        if trit == '2':
                            break
                        elif trit not in '01':
                            isError = True
                            return

                # param 없는 경우
                break

        if not matched:
            isError = True
            break

    return

# 스택에 넣는 함수
def push_stack(value):
    global stack, isError

    if len(stack) >= 1024:
        isError = True
        return
    stack.append(value)

# 콜스택에 넣는 함수
def push_call_stack(value):
    global call_stack, isError

    # 서브루틴도 1024 체크 필요
    if len(call_stack) >= 1024:
        isError = True
        return
    call_stack.append(value)

program = input().strip()

# 남은 입력은 전부 읽어 버퍼로 저장
input_buffer = list(sys.stdin.read())

# label 스캔
scan_labels()

# HALT 여부
halted = False

# 본 로직
while program_index < len(program) and not isError and not halted:

    matched = False
    
    for code, param, description in opcodes:

        if program.startswith(code, program_index):
            program_index += len(code)
            matched = True

            # 분기 시작
            if param == "number":
                # number면 프로그램에서 숫자 파싱
                value = parse_number_program()

                if description == "PUSH_POS":
                    push_stack(value)
                elif description == "PUSH_NEG":
                    push_stack(-value)

            elif param == "label":
                # 스캔해둔 레이블 이용
                label = read_label()

                if description == "LABEL":
                    # 레이블 스캔은 미리 했으므로 무시
                    pass

                elif description == "CALL":
                    if label not in labels:
                        isError = True
                        break
                    
                    push_call_stack(program_index)
                    program_index = labels[label]

                # 짬푸류 
                elif description == "JUMP":
                    if label not in labels:
                        isError = True
                        break

                    program_index = labels[label]
                elif description == "JUMP_IF_ZERO":
                    if not stack:
                        isError = True
                        break
                    condition = stack.pop()

                    if condition == 0:
                        if label not in labels:
                            isError = True
                            break

                        program_index = labels[label]
                elif description == "JUMP_IF_NEG":
                    if not stack:
                        isError = True
                        break
                    condition = stack.pop()

                    if condition < 0:
                        if label not in labels:
                            isError = True
                            break

                        program_index = labels[label]

            else:
                # 스택류
                if description == "DUP":
                    if not stack:
                        isError = True
                        break

                    push_stack(stack[-1])
                elif description == "SWAP":
                    if len(stack) < 2:
                        isError = True
                        break

                    stack[-1], stack[-2] = stack[-2], stack[-1]
                elif description == "DROP":
                    if not stack:
                        isError = True
                        break

                    stack.pop()
                elif description == "ADD":
                    if len(stack) < 2:
                        isError = True
                        break

                    a = stack.pop()
                    b = stack.pop()
                    
                    push_stack(a + b)
                elif description == "SUB":
                    if len(stack) < 2:
                        isError = True
                        break

                    a = stack.pop()
                    b = stack.pop()

                    push_stack(b - a)
                elif description == "MUL":
                    if len(stack) < 2:
                        isError = True
                        break

                    a = stack.pop()
                    b = stack.pop()

                    push_stack(a * b)
                elif description == "DIV":
                    if len(stack) < 2:
                        isError = True
                        break

                    a = stack.pop()
                    b = stack.pop()

                    if a == 0:
                        isError = True
                        break

                    #push_stack(b // a)
                    push_stack(int(b / a))
                elif description == "MOD":
                    if len(stack) < 2:
                        isError = True
                        break

                    a = stack.pop()
                    b = stack.pop()

                    if a == 0:
                        isError = True
                        break

                    #push_stack(b % a)
                    push_stack(b - int(b / a) * a)
                elif description == "STORE":
                    if len(stack) < 2:
                        isError = True
                        break

                    val = stack.pop()
                    addr = stack.pop()

                    heap[addr] = val
                elif description == "LOAD":
                    if not stack:
                        isError = True
                        break

                    addr = stack.pop()

                    push_stack(heap.get(addr, 0))
                elif description == "RETURN":
                    if not call_stack:
                        isError = True
                        break

                    program_index = call_stack.pop()
                    
                # 프린트류
                elif description == "PRINT_CHAR":
                    if not stack:
                        isError = True
                        break

                    output.append(chr(stack.pop() % 256))
                elif description == "PRINT_NUM":
                    if not stack:
                        isError = True
                        break

                    output.append(str(stack.pop()))
                elif description == "READ_CHAR":
                    if not stack:
                        isError = True
                        break

                    addr = stack.pop()
                    heap[addr] = read_char()
                elif description == "READ_NUM":
                    if not stack:
                        isError = True
                        break

                    addr = stack.pop()
                    heap[addr] = read_number()
                # 종료
                elif description == "HALT":    
                    halted = True
                    break
            break

    if not matched:
        # 남은 코드가 공백이거나 무의미하면 무시하고 종료
        if program_index >= len(program):
            break
        else:
            isError = True
            break

print(''.join(output), end='')

if isError:
    print("RUN-TIME ERROR", end='')
