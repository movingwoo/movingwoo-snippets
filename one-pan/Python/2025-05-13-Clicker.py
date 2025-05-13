import tkinter as tk  # GUI를 만들기 위한 기본 라이브러리
from PIL import Image, ImageTk  # 이미지를 처리 라이브러리
import random  # 랜덤한 숫자 생성 라이브러리, 랜덤 좌표를 생성하기 위함
import time  # 시간 지연을 위한 라이브러리
import threading  # 자동 클릭 스레드 처리를 위한 라이브러리
import pyautogui  # 마우스 자동 클릭을 위한 라이브러리
import os  # 파일 경로 처리를 위한 라이브러리

# 전역 변수로 클릭 횟수 저장
CLICK_COUNT = 0

# 메인 클래스
class Clicker:
    # 초기화 함수, 이름이 반드시 __init__이어야 동작함
    # self는 js로 치면 this
    # root는 외부 인자, tkinter로 만든 GUI 객체
    def __init__(self, root):
        
        self.root = root  # 메인 창 객체를 저장
        self.root.title("Clicker")  # 창의 제목
        self.root.geometry("800x600")  # 창의 크기
        
        # 게임이 진행될 캔버스를 생성
        self.canvas = tk.Canvas(root, width=800, height=600, bg="white")
        self.canvas.pack()  # 캔버스를 창에 배치해야 보임

        # 현재 실행 중인 파일의 경로
        current_dir = os.path.dirname(os.path.abspath(__file__))
        self.image_path = os.path.join(current_dir, "target.png")  # 이미지 파일의 전체 경로를 생성
        
        # 이미지 파일을 열고 tkinter에서 사용할 수 있는 형태로 변환
        self.image = Image.open(self.image_path)
        self.tk_image = ImageTk.PhotoImage(self.image)

        # 이미지 객체와 점수 텍스트를 저장할 변수를 초기화
        # 파이썬은 타입 선언만 하면 안되고 값 할당을 해야함
        self.image_obj = None
        self.score_text = None
        
        # 초기 이미지와 점수를 화면에 표시
        self.update_image()
        self.update_score()

        # 자동 클릭을 위한 스레드 시작
        # 스레드를 분리하지 않으면 클릭 함수가 실행되는 동안 GUI 창이 멈춤
        self.running = True  # 스레드 실행 상태를 저장, 프로그램 종료 시 스레드 종료
        threading.Thread(target=self.auto_click, daemon=True).start()  # 백그라운드에서 자동 클릭 스레드 실행

    # 이미지의 위치를 업데이트하는 함수
    def update_image(self):
        
        self.canvas.delete("all")  # 캔버스의 모든 요소를 제거
        # 화면크기에 딱 맞춰 배치하면 이미지가 잘림
        x = random.randint(0, 700)  # 0부터 700 사이의 랜덤한 x 좌표 생성
        y = random.randint(0, 500)  # 0부터 500 사이의 랜덤한 y 좌표 생성
        self.image_obj = self.canvas.create_image(x, y, anchor=tk.NW, image=self.tk_image)  # 새로운 위치에 이미지 생성
        self.update_score()  # 점수 표시 업데이트

    # 점수를 화면에 표시하는 함수
    def update_score(self):
        
        if self.score_text: 
            self.canvas.delete(self.score_text)  # 이전 점수 텍스트가 있으면 지우기
        self.score_text = self.canvas.create_text(
            400, 30,  # 화면 상단 중앙에 표시 (x=400, y=30)
            text=f"{CLICK_COUNT}",  # 현재 클릭 횟수
            font=("Arial", 20, "bold"),  # 글꼴
            fill="red"  # 색상
        )

    # 자동으로 이미지를 클릭하는 함수
    def auto_click(self):
        
        global CLICK_COUNT  # 전역 변수 CLICK_COUNT를 사용, global 선언하지 않으면 지역변수로 처리
        while self.running:  # running이 True인 동안 계속 실행
            try:
                # 화면에서 이미지 찾기 (70% 일치도)
                location = pyautogui.locateOnScreen(self.image_path, confidence=0.7)
                if location: 
                    # 이미지의 중앙 좌표를 계산
                    center_x = location.left + location.width // 2
                    center_y = location.top + location.height // 2
                    pyautogui.click(center_x, center_y)  # 해당 위치 클릭
                    CLICK_COUNT += 1  # 클릭 횟수 증가
                    # 이미지 위치 변경
                    self.root.after(0, self.update_image)
                time.sleep(0.1)  # 0.1초 대기 (클릭 간격 조절)

            except Exception as e:
                if str(e):  # 오류 메시지가 있을 때만 출력
                    print(f"자동 클릭 중 오류 발생: {e}")
                time.sleep(0.5)  # 오류 발생 시 0.5초 대기

    # 객체가 삭제될 때 실행되는 함수 (창을 닫을 때)
    def __del__(self):
        
        self.running = False  # 자동 클릭 스레드를 중지

# 프로그램의 시작점
# C나 java의 main()과 같음
if __name__ == "__main__":
    # 파이썬은 들여쓰기가 아주 중요함
    # 스페이스 4칸이 국룰이라고 함
    # 탭 누르면 4칸 들여쓰기가 됨

    root = tk.Tk()  # tkinter 창을 생성
    clicker = Clicker(root)  # 게임 객체를 생성
    root.mainloop()  # 창을 표시하고 이벤트 루프를 시작, 없으면 창이 열렸다가 바로 닫힘

    # 만약 창을 닫으면?
    # 1. root.mainloop() 종료
    # 2. clicker 객체가 더 이상 사용되지 않음
    # 3. __del__ 함수가 자동으로 호출됨
    # 4. self.running = False로 설정되어 자동 클릭 스레드가 종료됨
