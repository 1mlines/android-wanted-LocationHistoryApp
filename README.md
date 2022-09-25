# 원티드 프리온보딩 안드로이드
아래와 같은 협업 규칙, 컨벤션을 준수하며 코드 리뷰를 통해 이를 점검하고 코드 품질을 향상시켰습니다.
### #1 [협업 프로세스](https://dev-wotjd.notion.site/7bca0fa4e18a4715a32c88a5b340f48d)
### #2 [커밋 컨벤션](https://dev-wotjd.notion.site/Commit-Convention-811387d46db44c4cb270d267b5b760d8)
### #3 [코딩 컨벤션](https://www.notion.so/Coding-Convention-ab945c1216b0426b809c415c5fd5c869)
### #4 기술 스택
<img width="625" alt="image" src="https://user-images.githubusercontent.com/66052467/191895470-13d1eddc-d155-461f-9f41-67ae196893c4.png">

<br>

# 맡은 역할

## 이재성
### 1) Naver Map 연동 작업
<img src = "https://user-images.githubusercontent.com/51078673/191927063-2a07a089-afae-46a6-b04d-0178d2d9c58e.jpg" width = 300>

* Naver Map API를 이용한 지도 연동 작업을 진행하였습니다.

* 위치 갱신 버튼을 눌렀을 때 현재 위치로 카메라와 Position이 이동됩니다.

``` kotlin
private fun trackLocationChanged() {
    naverMap.addOnLocationChangeListener { location ->
        naverMap.locationOverlay.run {
            isVisible = true
            position = LatLng(location.latitude, location.longitude)
        }

        val cameraUpdate = CameraUpdate.scrollTo(
            LatLng(location.latitude, location.longitude)
        )

        naverMap.moveCamera(cameraUpdate)
    }
}
```

### 2) 도로명 주소 다이얼로그 구현
<img src = "https://user-images.githubusercontent.com/51078673/191930308-5783f545-e1b0-43a0-876c-40518f1a88c7.jpg" width = 300>

* 좌표(위치, 경도)에 따른 도로명 주소를 Geocoder API를 이용하여 구현하였습니다.

``` kotlin
private fun convertLocationToAddress(latitude: Double, longitude: Double): String {
    val geoCoder = Geocoder(this, Locale.KOREA)
    val address: ArrayList<Address>

    var result = "결과가 없습니다."

    try {
        address = geoCoder.getFromLocation(latitude, longitude, 1) as ArrayList<Address>
        if (address.size > 0) {
            val currentLocationAddress = address[0].getAddressLine(0).toString()
            result = currentLocationAddress
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return result
}
```

### 3) 협업 규칙 선정 및 관리

* 자주 사용하던 Git 브랜치 전략, Git 컨벤션, 코딩 컨벤션들을 팀원에게 소개하고, 많은 대화를 통해 협업 시 사용할 적절한 규칙들을 결정하였습니다.
* Pull Request가 올라올 시 선정한 협업 규칙에 위배되는 코드나 코딩 스타일을 최대한 확인하며 코드리뷰를 진행하였습니다.

<img src = "https://user-images.githubusercontent.com/51078673/191929693-880b2098-19b7-49de-95c1-f9a2b42c08bd.png">

### 회고
아쉬움이 굉장히 많이 남는 프로젝트라고 생각합니다. 팀원들과 기간동안 정말 많은 대화를 했음에도 불구하고 성공적으로 마무리하지 못했기 때문입니다.

개인적으로는 Location과 Map에 대한 경험과 이해도가 부족해 주어진 시간 내에 효율적으로 구현하지 못했던 것이 많이 아쉽고 팀적으로는 팀원들의 개인 역량과 경험을 고려하지 못한 채 무리한 아키텍쳐 선정과 작업 배분을 했던 것 같습니다.

하지만, 팀에서 선정한 협업 규칙들과 컨벤션을 최대한 지키기 위해 노력했고, 이를 위해 수준 높은 코드리뷰는 아니지만 현업에 유사한 프로세스를 프로젝트에 적용시켜 봤다는 점에서는 만족합니다.

<br>

## 김영진
### 1) History 저장 구현 (Feat. WorkManager)

[[FEAT] History 저장 구현 #3](https://github.com/Wanted-Pre-Onboarding-Android-Team-3/android-wanted-LocationHistoryApp/issues/3)
- 백그라운드, 포그라운드 모두에서 일정 기간동안, 반복되는 Work를 수행하기 위해 Jepack WorkManager를 사용하였습니다.
- SaveHistoryWorker
  - LocationUtil.getCurrentLocation() 함수로 location 받기
  - location이 없다면 retry 반환하여 설정한 재시도 정책에 따라 10초 후 Worker 재실행
  - location이 있다면 saveHistory 함수에서 room에 insert History (미구현)
```kotlin
class SaveHistoryWorker(tempContext: Context, params: WorkerParameters) :
    Worker(tempContext, params) {

    private val context: Context by lazy {
        tempContext
    }

    override fun doWork(): Result {
        LocationUtil.setCurrentLocation(context)

        return when (val currentLocation: Location? = LocationUtil.getCurrentLocation()) {
            null -> Result.retry()
            else -> {
                saveHistory(currentLocation)
                Result.success()
            }
        }
    }

    private fun saveHistory(currentLocation: Location?) {
        currentLocation?.let {
            Timber.d("$it")
            /*todo
           * room saveHistory
           * */
        }
    }

}
```
- WorkManagerUtil
  - MainActivity 코드가 비대해져 WorkManagerUtil로 분리하여 가독성을 높임
  - WorkRequest 생성, WorkManager 생성, Work 관리
```kotlin
class WorkMangerUtil(context: Context) {

    private val workManager: WorkManager by lazy { WorkManager.getInstance(context) }
    private val constraint: Constraints by lazy {
        Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiresStorageNotLow(true)
            .build()
    }

    private fun getSaveHistoryWorkRequest(): PeriodicWorkRequest =
        PeriodicWorkRequestBuilder<SaveHistoryWorker>(
            PreferencesUtil.getSaveHistoryPeriod().toLong() + 5L,// period + 5 - FLEX TIME(5) 부터 work 시작
            TimeUnit.MINUTES
        )
            .setConstraints(constraint) // 작업을 재시도 할경우에 대한 정책
            .addTag(Constants.WORK_SAVE_HISTORY)
            .setBackoffCriteria( // 최소 시간(10초)으로 Retry
                BackoffPolicy.LINEAR,
                PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

    fun startSaveHistoryWork() {

        val saveHistoryWorkRequest: PeriodicWorkRequest = getSaveHistoryWorkRequest()

        workManager.enqueueUniquePeriodicWork(
            Constants.WORK_SAVE_HISTORY,
            ExistingPeriodicWorkPolicy.REPLACE, //동일한 이름의 Work Replace
            saveHistoryWorkRequest
        )
    }
}
```

- MainActivity
  - onCreate()에서 SaveHistoryWorker 시작 -> 위치 권환 확인 이후 SaveHistoryWorker 시작으로 변경 예정 
  - History 저장 기간 변경되는 경우 SaveHistoryWorker 시작
```kotlin
    private val workManagerUtil: WorkMangerUtil by lazy { WorkMangerUtil(this) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        workManagerUtil.startSaveHistoryWork()
    }
    
    private fun registerOnSharedPreferenceChangeListener() {
        PreferencesUtil.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }
    
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        key?.let {
            if (it == SAVE_HISTORY_PERIOD_KEY) {
                workManagerUtil.startSaveHistoryWork()
                Timber.d("abcabc: ${PreferencesUtil.getSaveHistoryPeriod()}")
            }
        }
    }
```

### 2) History 저장 구현 (Feat. WorkManager)
[[FEAT] 설정 Dialog 구현 #15](https://github.com/Wanted-Pre-Onboarding-Android-Team-3/android-wanted-LocationHistoryApp/issues/15) <br>
<img src = "https://user-images.githubusercontent.com/66052467/191706493-b64f8f54-b808-44e1-acf1-3b173d3fa2a7.gif" width = 300>
- 저장 간격 15분 ~ 60분
  - default 값 15분 
- validation Check 구현

- PreferencesUtil
  - 저장 간격을 위한 PreferencesUtil
  - MainActivity 코드가 비대해져 PreferencesUtil로 분리하여 가독성을 높임
  
```kotlin
object PreferencesUtil {

    lateinit var sharedPreferences: SharedPreferences

    //saveHistoryPeriod 초기값 15
    fun initSharedPreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences(
            Constants.SHARED_PREFERENCES,
            Application.MODE_PRIVATE
        )
        val initSaveHistoryPeriod: Int = sharedPreferences.getInt(SAVE_HISTORY_PERIOD_KEY, 0)
        if (initSaveHistoryPeriod == 0) {
            sharedPreferences.edit().apply {
                putInt(SAVE_HISTORY_PERIOD_KEY, Constants.SAVE_HISTORY_PERIOD_MIN)
                apply()
            }
        }
    }

    fun setSaveHistoryPeriod(period: Int) {
        sharedPreferences.edit().apply {
            putInt(SAVE_HISTORY_PERIOD_KEY, period)
            apply()
        }
    }

    fun getSaveHistoryPeriod(): Int = sharedPreferences.getInt(
        SAVE_HISTORY_PERIOD_KEY,
        WORK_REPEAT_INTERVAL_DEFAULT
    )

}
```

#### AnimationUtil
- 저장 간격 Validation Check에 사용되는 Animation
```kotlin
object AnimationUtil {
    fun shakeAnimation(context: Context): Animation = AnimationUtils.loadAnimation(context, R.anim.shake)
}
```

### 회고
Issue, Project 등의 Github 기능들을 사용하고, 협업 규칙, 코딩 컨벤션, 커밋 컨벤션을 준수하며 체계적인 협업 프로세스로 작업한 것이 가장 큰 수확이었습니다. <br>

서로의 코드를 리뷰하고 설계적인 부분을 논의하며 서로 모르는 부분에 대해 도움을 요청하며 비록 기능은 미흡하지만 많이 성장할 수 있었습니다. <br>

프로젝트에서 크게 신경썼던 부분은 Activity의 코드를 분리하는 것, 컨벤션을 맞추며 협업 프로세스에 익숙해지는 것이었습니다.

개인적인 아쉬움은, 프로젝트 초반, WorkManager의 작업이 Main Thread에서 돌아가는 것을 확인했음에도 이에 대한 비동기 처리를 하지 않은 점, <br>
CoroutineWork에 대해 더 알아보지 못한 점, 그리고 추가 기능으로 화면에 History 저장 기간이 타이머로 노출되는 UI를 구현하지 못한 것이 아쉬움으로 남습니다. <br>


## 박인아

1.인트로 화면
   시간상 구현하지 못하였습니다.
 - Android 12 이전 : Splas화면을 xml로 작성한 후, Manifest에서 테마 지정 후,   MainActivity의 on Create 호출시, 테마 적용.
 - Android 12 이후 : Splash API 구현 예정이였습니다. Api 31로 Themes.xml 작성 후, splashScreen.setOnExitAnimationListener 를 통해 다음 작업 처리.
 위와 같은 방식으로 구현하고자 하였습니다.

2.Location & Background Permission

- FINE_LOCATION 과 COARSE_LOCATION 위치 권한 받기
- Android 10 이상 버전일 시, 위치 권한 받은 후, Background 권한 체크
- Android 9 이하 버전일 시, 위치 권한 체크

<pre>
<code>

    fun checkLocationPermission(){
        requestMultiplePermissions.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

</code>
</pre>


<pre>
<code>

    private val requestMultiplePermissions : ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var granted: Boolean = true
            permissions.entries.forEach {
                if (!it.value) {
                    granted = false
                }
            }
            if (granted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    backgroundLocationPermission(222)
                } else {
                   // Location Update
                }
            } else {
                Toast.makeText(this, "서비스를 사용하시려면 위치 추적이 허용되어야 합니다.,", Toast.LENGTH_LONG)
                    .show()
            }
        }

</code>
</pre>
- Android 11 이상

위치 권한 먼저 받은 후, 백그라운드 권한 허용

<img src = "https://user-images.githubusercontent.com/95750706/191770021-9759e4d2-b8e1-4188-970d-e8b8d8274ee7.png" width = 300>   <img src = "https://user-images.githubusercontent.com/95750706/191773316-04f348fc-575f-4bd0-a857-7de4890df316.png" width = 300> <img src = "https://user-images.githubusercontent.com/95750706/191772560-c5bb92c5-9c75-4029-a0aa-c58614cd1d62.png" width = 300>


- Android 10 이하


<img src = "https://user-images.githubusercontent.com/95750706/191771999-7b8b5910-9263-400a-b319-02f6084067fd.png" width = 300>  <img src = "https://user-images.githubusercontent.com/95750706/191773316-04f348fc-575f-4bd0-a857-7de4890df316.png" width = 300> <img src = "https://user-images.githubusercontent.com/95750706/191772560-c5bb92c5-9c75-4029-a0aa-c58614cd1d62.png" width = 300>


- Android 9 이하

<img src = "https://user-images.githubusercontent.com/95750706/191874343-a3fad7b2-1b31-48c8-9f49-655f5aab7c4e.png" width = 300>

프로젝트를 마치며..
 : 평소 Git에 대한 경험이 많이 없어서 많이 헤메었지만, 이번 팀 원을 만나 체계적인 협업을 같이 해보았던 것이 정말 큰 경험이 되었습니다.
   프로젝트를 진행하며 많이 미숙한 부분에 대해서, 팀원들이 다독여주고 화면 공유를 통해 차분이 알려주며 저를 이끌어 준 것에 고맙고, 덕분에 많은 경험이 쌓였습니다.
## 황준성

- 맡은 부분
    - History 보기 기능
-  기여한 점
    - Dialog 생성
    - RoomDB 생성
    - ViewModel,Repository,Factory 기능 적용
-  아쉬운 점
    - MVVM,AAC 패턴을 제대로 구현하지 못한것 같아 아쉽습니다.
    ViewModel 과 Repository,Factory등에 대한 이해가 부족해서
    다음 과제에는 좀 더 학습후에 진행하고 싶습니다.

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
