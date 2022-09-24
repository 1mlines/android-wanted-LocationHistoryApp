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
                  <img alt="lsy524" src="https://avatars.githubusercontent.com/lsy524" width="80" />            
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

### 임성용
- 맡은 부분
  - 인트로 화면 
- 기여한 점 
  - `Splash Screen` 구성 
  - 3초 후 메인화면 이동 
  - 네트워크 연결 상태 체크
- 아쉬운 점
  - Splash Screen Icon에 Animation 기능을 추가하지 못했습니다.
  
| Splash Screen | 위치 설정 권한 거부 | 네트워크 연결 상태 체크 |
| :------: | :--------: | :--------------: |
| <video src = "https://user-images.githubusercontent.com/96644159/191818822-cf0c7642-2e5b-4c34-aa3c-b3577400c9e8.mp4"> | <video src ="https://user-images.githubusercontent.com/96644159/191819950-03085e39-2016-46f3-abf0-419a6e3663ea.mp4"> |  <video src ="https://user-images.githubusercontent.com/96644159/191819965-4253c344-1170-4bbf-8fd1-3a6f8d87597e.mp4"> |
  
```kotlin
    private fun initData() {
        thread(true) {
            for (i in 1..3) {
                Thread.sleep(1000)
            }
            isReady = true
        }
    }
```
- 별도의 작업은 수행하지 않고 3초간의 딜레이를 주고 이후 작업을 진행합니다. 

  
```kotlin
    private fun initSplashScreen() {
        initData()

        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (isReady) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        if (!isConnection()) {
                            Snackbar.make(
                                binding.root,
                                R.string.networkError,
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                        checkPermission()
                        true
                    } else {
                        false
                    }
                }
            }
        )
    }
```
- `addOnPreDrawListener`를 통하여 `Splash Screen`이 그려지는 것에 대한 결과를 받습니다.
- `onPreDraw`는 `Splash Screen`이 보이는 동안 계속해서 호출되며, `isReady`가 true일 때 true를 반환하면서 해당 스크린을 지우도록 처리하였습니다.
- `isReady`는 `initData`에서 3초 뒤에 true로 변경하도록 설정해두었습니다. 그로인해, `Splash Screen`이 3초 후에 제거됩니다.

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



#### 맵 연동
*map.html*
![image](https://user-images.githubusercontent.com/45396949/191758710-23f381ef-862a-4ee8-a42a-bdeda6b3da20.png)
- assets 파일에 html 파일을 구성하여 apk에 포함되도록 구성했습니다.
- 웹뷰에서 호출하는 html으로, 비동기적으로 JavaScript에서 구현한 initMap()를 호출하여 Map을 구성합니다. 

*javascript* 

![image](https://user-images.githubusercontent.com/45396949/191758329-9bd6a89b-7214-425d-86f3-eabb6c5055d9.png)
- zoom: 20 으로 건물이 보이는 정도로 거리를 유지했습니다.
- disableDefaultUI: true 로 지정하여 기본적으로 구성하는 버튼을 사용하지 않도록 만들었습니다.

#### 히스토리 불러오기

*LocalDataSource*

![image](https://user-images.githubusercontent.com/45396949/191771361-7d30691a-35b6-402d-9af9-32a992e900e0.png)
- Room DB를 flow를 이용하여 변경이 발생했을 시, 데이터를 방출할 수 있는 구조로 설계했습니다. 

*WebViewBridge*

![image](https://user-images.githubusercontent.com/45396949/191887451-64d85b0e-4048-4fe1-a936-ba3c70bcbcd0.png)

- Activity의 메서드를 호출해야하는 부분을 interface로 만들고 힐트로 부터 주입받도록 구현했습니다. 
- Coroutine을 관리하기 위해 SupervisorJob과 CoroutineExceptionHandler 기능을 갖는 CoroutineContext를 만들어서 사용했습니다.

![image](https://user-images.githubusercontent.com/45396949/191887478-b3233d8f-b335-437e-83e4-8d869cd7da2f.png)

- WebViewBridge를 통해 collect로부터 전달받은 history 정보를 Map에 전달하며 Coroutine에서 동작하도록 만들었습니다.

*JavaScriptUrlUtil*

![image](https://user-images.githubusercontent.com/45396949/191772451-c7d92157-7015-4423-b6cf-0b96dea6f373.png)

- Util 클래스를 통해서 webView 요청을 위한 Url을 생성합니다.
- javascript를 호출하기 위한 url로, error 처리를 위해서 try/catch 문으로 작성하였습니다. 

*JavaScript*

![image](https://user-images.githubusercontent.com/45396949/191773136-2b3aaf6d-69c3-4f4f-8a84-997064c6a367.png)

- url을 통해 실행될 JavaScript 파일입니다.
- bridge를 통한 데이터 전달은 직렬화를 통해서만 가능하기 때문에 javascript에서 다시 역직렬화하여 객체값으로 구성했습니다.
- room에 저장된 위/경도에 따라 마커를 추가하도록 합니다. 

### 김정호

- 맡은 부분
    - 설정 다이얼로그
    - 히스토리 다이얼로그 (미완)
- 기여한 점
    - `BaseDialog` 구성
    - `WorkManager` 를 통해 `SharedPreferences` 에 유저가 설정한 저장 간격을 저장
    - `WorkManager` 에서 `Hilt` 를 사용할 수 있도록 구성
- 아쉬운 점
    - `WorkManager` 를 좀 더 제대로 알았다면 클린한 코드가 완성됐을 것 같다.
    - 협업 컨벤션을 명확히 지정하고 프로젝트에 임했다면 더 좋았을 것 같다.

![Screenshot_1663867521 중간](https://user-images.githubusercontent.com/85336456/191812406-3c050777-078a-4a86-82fc-86adc1857f24.jpeg)

- 취소 버튼을 누르거나 다이얼로그 외부 영역을 클릭하면 다이얼로그가 사라집니다.
- 확인 버튼을 누르면 저장 간격을 내부 데이터베이스에 저장하고, 간격에 따라 현 위치를 내부 데이터베이스에 저장하는 동작을 시작합니다.

```kotlin
        binding.confirm.setOnClickListener {
            val interval = binding.etMinute.text.toString().toLong()

            val workRequest =
                PeriodicWorkRequestBuilder<CurrentLocationWorker>(interval, TimeUnit.MINUTES)
                    .build()
            workManager.enqueue(workRequest)

            viewModel.updateStorageInterval(interval)
            dismiss()
        }
```

- 저장 버튼을 눌렀을 때 동작하는 로직입니다.
- `workManager` 를 `viewModel` 에 넣을지도 고민했지만, `viewModel` 에서는 안드로이드 의존을 최대한 피하는게 좋겠다고 판단하여 `DialogFragment` 에서 사용하였습니다.

```kotlin
@HiltWorker
class CurrentLocationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted parameters: WorkerParameters,
    private val locationRepository: LocationRepository
) : CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result {

        lateinit var result: Result

        coroutineScope {
            withContext(Dispatchers.IO) {
                if (checkPermission()) {
                    val manager: LocationManager =
                        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                    val currentLocation: android.location.Location? =
                        manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                    val latitude = currentLocation?.latitude
                    val longitude = currentLocation?.longitude
                    val currentTimestamp = Timestamp(System.currentTimeMillis()).time

                    val location = if (latitude != null && longitude != null) {
                        Location(0, latitude.toFloat(), longitude.toFloat(), currentTimestamp)
                    } else {
                        Location.EMPTY
                    }

                    insertLocation(location)

                    result = Result.success()
                } else {
                    result = Result.failure()
                }
            }
        }

        return result
    }

    private fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && 
                ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private suspend fun insertLocation(location: Location) {
        locationRepository.insertLocation(location)
    }
}
```

- 설정 간격에 따라 내부 데이터베이스에 현 위치를 저장하는 로직입니다.
- 안드로이드 기기에서 GPS를 통해 현 위치를 추적합니다.
- `js`, `webView` 로 앱을 구성한만큼 `js` 의 로직으로 현 위치를 불러오고 싶었으나, 시간 상 여유가 부족하여 더 나은 로직을 발견하지 못했습니다.

![History Dialog](https://user-images.githubusercontent.com/85336456/191920937-bd8633b3-3e98-448c-9555-fbf4da8a7eb5.jpeg)

- 히스토리 다이얼로그에서는 내부 데이터베이스에 저장된 위치 정보를 확인할 수 있습니다. (시간 상의 이유로 날짜별 리스트 출력 미구현)
