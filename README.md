# Team-4 : [1주차 과제] 위치정보를 표시할 수 있는 안드로이드 앱 개발

<br>

## 심준보

### 맡은 일
- 메인화면 UI 작업
- 구글 맵 연결
- 사용자 위치 권한 요청 작업
- 현 위치 표시 작업
- 메인 히스토리 마커 표시 작업
- 현 위치 저장 작업
- 해당 날짜 히스토리 마커 표시 작업

### 구글 맵 연결 작업
- 구글맵 api를 이용하여 연결 작업

### Permission

- Location permmision
  - ACCESS_COARSE_LOCATION : 인터넷 데이터로 사용자 위치 정보를 가져오기 위함
  - ACCESS_FINE_LOCATION : 인터넷 데이터 + GPS로 사용자 위치 정보를 가져오기 위함

### 현 위치 표시 작업

Google Play Service의 Location 라이브러리 사용

### 마커 표시 작업

local DB를 이용하여 위도 경도 데이터들을 처리

<br><hr><br>

## 이상준

### 맡은 일
- 인트로 화면 구현
- Setting Dialog, Address Dialog 생성
- 입력한 시간 간격마다 DB 저장
- 현 위치 주소 변환

### 인트로 화면
- Handler를 이용하여 3초 딜레이 후 화면 전환

### 입력한 시간 간격마다 현 위치 DB 저장
- FusedLocationProviderClient 이용하여 현 위치값 얻기

### 현 위치 주소 변환
- GeoCoder를 이용하여 현 위치를 주소로 변환

### 어려웠던 부분
- 백그라운드, 포그라운드에서 주기적으로 DB에 저장

<br><hr><br>

## 한혜원

### 맡은 일
- Db, Entity 생성
- Dao에 히스토리 저장, 불러오기 쿼리문 작성
- repository 패턴을 사용하여 구현

### 어려웠던 부분
- 현 위치로 부터 특정 거리 안에 존재하는 히스토리 마커 찾기
- 현 위치가 저장된 시간에 관련된 컬럼 설정하기

### 신경썼던 부분
- 테스트 코드 작성
- [android/architecture-samples](https://github.com/android/architecture-samples/tree/livedata/app/src/main/java/com/example/android/architecture/blueprints/todoapp/data/source) 참고하여 OOP 지켜보기

<br><hr><br>

## 박성호

### 맡은 일
- 기본 커스텀 다이얼로그 레이아웃 구성하기
- Date Dialog 호출
- 처음에 다이얼로그 실행 시 오늘 날짜 출력
- 날짜 선택 시 날짜 변경
- 날짜에 맞는 히스토리 리사이클러뷰에 출력
- 확인 버튼 누를 시 웹뷰에 해당 날짜 히스토리 마커 출력


### 1. 초기 실행 시 - 현재 날짜의 히스토리 정보들을 출력
<img src="https://user-images.githubusercontent.com/42669772/191744963-1c8337fe-2da1-42c6-b7f0-9aa782ac7e4f.PNG" width="200" height="400"/> 


### 2. 날짜 변경 - 날짜 버튼을 클릭해서 다른 날짜로 이동하면 해당 날짜의 히스토리 정보들을 출력
<img src="https://user-images.githubusercontent.com/42669772/191745195-a69f5039-f1e0-4265-871e-9923bd74657e.PNG" width="200" height="400"/> <img src="https://user-images.githubusercontent.com/42669772/191745259-1d72ce85-6c96-484e-8dea-b948a435b34b.PNG" width="200" height="400"/>


### 3. 신경쓴 부분
- 스크린의 크기를 구해서 각 기기의 비율에 맞게끔 설정
- 기존에 사용하던 defaultDisplay 함수가 API 30부터 deprecated되어서 windowMetrics를 이용하여 스크린의 사이즈를 구함

<img src="https://user-images.githubusercontent.com/42669772/191745846-e98a53aa-1d11-41ed-ba98-33493ee47ff8.PNG" height="350"/>
<img src="https://user-images.githubusercontent.com/42669772/191746113-858dc8b0-2b80-4b2c-a99d-dddfafe876be.PNG" height="200"/>

### 4. 남은 구현

확인 버튼을 눌렀을 때 메인 화면에 히스토리 정보에 맞는 마커들을 출력해주는 기능




