# 원티드 프리온보딩 안드로이드
아래와 같은 협업 규칙, 컨벤션을 준수하며 코드 리뷰를 통해 이를 점검하고 코드 품질을 향상시켰습니다.
### #1 [협업 프로세스](https://dev-wotjd.notion.site/7bca0fa4e18a4715a32c88a5b340f48d)
### #2 [커밋 컨벤션](https://dev-wotjd.notion.site/Commit-Convention-811387d46db44c4cb270d267b5b760d8)
### #3 [코딩 컨벤션](https://www.notion.so/Coding-Convention-ab945c1216b0426b809c415c5fd5c869)

# 맡은 역할

## 이재성

## 김영진

## 박인아

## 황준성

저는 과제의 기능중에서 히스토리를 맡았습니다.

히스토리 보기를 맡아서 RoomDB를 설계하고,

SetHistoryDialog를 통해 저장된 위치를 RoomDataBase에 저장하고

이를 다시 RecyclerView에 bind 합니다.

그리고 해당 데이터를 현재 지도에 mark 하는 과정입니다.

DB에서는 위치를 찍을 좌표와 좌표가 생성된 현재 시간 그리고 num 기본키를 저장하고

HistoryDao 에서는 날짜를 이용한 Query로 RoomDataBase로 부터 해당 날짜와 생성된 시간이 같은 Data만을 가져오게 하는 getHistory와 좌표를 입력할때 사용할 insertHistory를 구현하였습니다.

이번에 처음으로 클린아키텍쳐 와 MVVM 패턴을 사용하여 RecyclerView 생성시 ViewModel과 Repository,Factory,RepositoryIpml 을 사용해보았습니다.

AAC ViewModel 을 상속해서 메인 액티비티에서 이를 사용하게 하고

repository를 매개변수로 받아 해당 메소드 들을 호출하게 하였습니다.

LiveData 변수들을 했고, AAC ViewModel 의 경우 ViewModelProvider를 이용해 초기화를 하는데 이때 매개변수를 전달하기 위해서 ViewModelFactory를 사용해 보았습니다.

## 기술 스택
<img width="625" alt="image" src="https://user-images.githubusercontent.com/66052467/191895470-13d1eddc-d155-461f-9f41-67ae196893c4.png">
