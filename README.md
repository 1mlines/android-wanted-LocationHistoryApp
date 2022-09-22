# 원티드 프리온보딩 안드로이드 5팀

## Convention

### Branch Naming

- `{behavior}/ticket-{number}-{something}`
- e.g. : `feature/ticket-007-data-module`

### Commit

- `feat : {something}` : 새로운 기능을 추가했을 때
- `fix : {something}` : 기능 중 버그를 수정했을 때
- `design : {something}` : 디자인 일부를 변경했을 때
- `refactor : {something}` : 코드를 재정비 하였을 때
- `chore : {something}` : 빌드 관련 업무를 수정하거나 패키지 매니저 수정했을 때
- `docs : {something}` : README와 같은 문서를 변경했을 때
- `test : {something}` : 누락된 테스트 코드를 추가했을 때

## Members

<div align="center">
  <table style="font-weight : bold">
      <tr>
          <td align="center">
              <a href="https://github.com/tjrkdgnl">                 
                  <img alt="tjrkdgnl" src="https://avatars.githubusercontent.com/tjrkdgnl" width="80" />            
              </a>
          </td>
          <td align="center">
              <a href="https://github.com/gyurim">                 
                  <img alt="gyurim" src="https://avatars.githubusercontent.com/gyurim" width="80" />            
              </a>
          </td>
          <td align="center">
              <a href="https://github.com/lsy524">                 
                  <img alt="윤여준" src="https://avatars.githubusercontent.com/lsy524" width="80" />            
              </a>
          </td>
          <td align="center">
              <a href="https://github.com/choieuihyun">                 
                  <img alt="choieuihyun" src="https://avatars.githubusercontent.com/choieuihyun" width="80" />            
              </a>
          </td>
          <td align="center">
              <a href="https://github.com/hoyahozz">                 
                  <img alt="hoyahozz" src="https://avatars.githubusercontent.com/hoyahozz" width="80" />            
              </a>
          </td>
      </tr>
      <tr>
          <td align="center">tjrkdgnl</td>
          <td align="center">gyurim</td>
          <td align="center">lsy524</td>
          <td align="center">choieuihyun</td>
          <td align="center">hoyahozz</td>
      </tr>
  </table>
</div>

## Architecture
<img width="1441" alt="스크린샷 2022-09-22 오후 9 22 03" src="https://user-images.githubusercontent.com/31344894/191757954-3107453e-60fd-4be0-be37-266e7525680d.png">
전체 아키텍처는 Clean Architecture를 적용하였습니다.

### Presentation

- UI와 관련된 파일들이 저장되어 있으며 Activity, DialogFragment, WebViewBridge, ViewModel 등으로 구성되어 있습니다.
- MVVM 패턴을 사용합니다.

### Domain

- 비즈니스 영역과 데이터 영역에서 사용되는 로직을 제공하는 패키지로 interface 및 model로 구성되어 있습니다.
- repository
    - Data layer에 접근하여 데이터를 처리할 수 있는 인터페이스를 정의하고 있습니다.
- model
    - 실제 UI에 적용되는 비즈니스 데이터를 담고 있습니다.

### Data

- Repository
    - RepositoryImpl에는 도메인에서 정의한 repository 인터페이스가 구현되어있습니다.
    - Datasource는 도메인에서 데이터에 접근할 수 있도록 해줍니다.
- DB
    - RoomDatabase, Dao, Entity가 구성되어 있습니다.
- mapper
    - Entity와 Model 사이 변환을 위한 확장 함수가 구현되어 있습니다.
- DI
    - Hilt 라이브러리를 사용해 의존성 주입을 위한 Module이 구성되어있습니다.
    - RoomModule, GsonModule, RepositoryModule

## Library
### Di
- Hilt

### Database
- Room

### Asynchronous
- Coroutine, Flow

### 박규림
- 맡은 부분
    - Base Architecture 작성
- 기여한 점
    - RoomDatabase 연동
    - Repository 패턴 적용
    - DI를 위한 Module 구성
- 아쉬운 점
    - 이번 프로젝트를 진행하며 팀원들이 기능 구현에 어려움을 겪는 걸 봤는데, Base Architecture 구성을 맡음으로써 기능 구현에 도움이 많이 못 된 것이 아쉽습니다.

### 서강휘 
- 맡은 부분
  - 히스토리 표시하기
- 기여한 점 
  - WebView와 Google Map JavaScript API 연동
  - WebViewBridge 개발 및 Android Component와 연동
  - Map 구성을 위해 HTML, JavaScript, CSS 작성
- 아쉬운 점
  - WebView 최적화를 진행하지 못해 성능이 많이 아쉽습니다. 
  - 현재 ViewPort에 해당하는 History만 가져올 수 있도록 구현하지 못했습니다.
  - 현재 위치를 표시하는 버튼을 별도로 구현하지 못했습니다. 
  
| 맵 연동 | 마커 추가 | 히스토리 불러오기 |
| :------: | :--------: | :--------------: |
| <video src = "https://user-images.githubusercontent.com/45396949/191752443-364e25c4-597c-4cdc-8d97-d584cce70402.mp4"> | <video src ="https://user-images.githubusercontent.com/45396949/191754293-7f0b01f0-73ff-4ed8-ba78-cc5ad83c9022.mp4"> |  <video src ="https://user-images.githubusercontent.com/45396949/191753928-f124f596-5f5a-4e50-ab8c-99003a93ef65.mp4"> |









