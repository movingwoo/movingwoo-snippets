#!/bin/bash

echo "================================================="
echo "스크립트 실행 경로 내 복사할 파일이 있어야 함!!"
echo "================================================="
read -p "파일명 입력 (확장자 포함) : " FILENAME

if [ ! -e "$FILENAME" ]; then
  echo "파일이 없음..."
  exit 1
fi

read -p "복사할 개수 (숫자) : " COUNT

if [[ ! $COUNT =~ ^[0-9]+$ ]]; then
  echo "숫자가 아님..."
  exit 1
fi

read -p "$FILENAME 을(를) $COUNT 번 복사하는게 맞으면 y : " BOOL

if [[ ! "${BOOL,,}" == "y" ]]; then
  exit 1
fi

for i in $(seq 1 $COUNT)
do
  cp -r "$FILENAME" "${FILENAME%.*}$i.${FILENAME##*.}"
  echo "$i/$COUNT 진행 중.."
done

echo "$COUNT 개 복사 완료!!"
