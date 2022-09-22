# Team 1 (김현수, 권혁준, 이현섭)

# 🔧 Tech

- MVVM
- Room
- Hilt
- Kakao Map
- DataBinding

# 🗂️ Structure

~~~
├─base
├─data
│  ├─database
│  ├─entity
│  └─repository
├─di
├─feature
│  ├─history
│  │  ├─domain
│  │  │  └─usecase
│  │  └─presentation
│  ├─map
│  │  └─presentation
│  ├─presentation
│  └─set
└─shared
~~~

- data
    - RoomDatabase 빌더 및 Dao
    - repository
        - DB로부터 날짜별 히스토리 가져옴
        - 히스토리 DB에 저장
- di
    - Repository bind
    - Database 및 Dao provide
- feature
    - history
        - doamin
            - UseCase 작성 및 Repository Interface 작성
        - 히스토리 리스트 어댑터 , 바인딩 어댑터 , 히스토리 다이얼로그 작성
    - map
        - BallonAdapter를 통해 마커에 주소 표기
    - set
        - 세팅 시간 저장을 위한 다이얼로그
- shared
    - SharedPreference로 세팅 시간 관리

# 🤷‍♂️Remaining Work

- UI 수정 및 다듬기
- 스플래시
- 코드 정리

# 📱 Result (until now)
![Screenshot_20220923-032705_android-wanted-LocationHistoryApp](https://user-images.githubusercontent.com/86879099/191828394-4cbd851e-7e54-425f-9084-7b8fc0d03e22.jpg)![Screenshot_20220923-032757_android-wanted-LocationHistoryApp](https://user-images.githubusercontent.com/86879099/191828505-1609296e-f069-4aa2-abdb-4df6a2e5001d.jpg)

- 마커

![Screenshot_20220923-032814_android-wanted-LocationHistoryApp](https://user-images.githubusercontent.com/86879099/191828710-566589e0-faf3-41c9-89ba-f121cf77b8f7.jpg)

- 현재 위치

![Screenshot_20220923-032742_android-wanted-LocationHistoryApp](https://user-images.githubusercontent.com/86879099/191828806-9f8726ea-0403-4b2e-a562-5d40879c221a.jpg)![Screenshot_20220923-032746_android-wanted-LocationHistoryApp](https://user-images.githubusercontent.com/86879099/191828823-29761027-1240-4718-a3c5-c7bba80eb4d7.jpg)
- 날짜별 히스토리

![Screenshot_20220923-032806_android-wanted-LocationHistoryApp](https://user-images.githubusercontent.com/86879099/191828932-73b2adee-665d-487d-b884-1250a062a538.jpg)
- 시간 설정
