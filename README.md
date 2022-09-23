# 원티드 프리온보딩 안드로이드

  * [1. Summary](#1-summary)
  * [2. People](#2-people)
  * [3. Architecture](#3-architecture)
  * [4. Feature & Screen](#4-feature-&-screen)
  * [5. Technology Stack](#5-technology-stack)
  * [6. Git Convention](#6-git-convention)

## 1. Summary

<img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=Android&logoColor=white"> <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=Kotlin&logoColor=white">

> 원티드 프리온보딩 1차 기업 과제

> 위치 정보를 표시할 수 있는 안드로이드 앱 서비스

## 2. People

|<img width=150 src="https://user-images.githubusercontent.com/85485290/191734529-c24986a5-3cde-423e-a834-ed2a392f86ef.png" />|<img width=150 src="https://user-images.githubusercontent.com/85485290/191734490-31b956b0-85fb-4f18-b2c1-f4212fccdd46.png" />|<img width=150 src="https://user-images.githubusercontent.com/85485290/191734505-e5be8b0d-86e7-48f1-a673-716ff00272a0.png" />|<img width=150 src="https://user-images.githubusercontent.com/85485290/191734561-ca8cd518-d774-4455-b9cf-c252317871f7.png" />|
|:----:|:----:|:----:|:----:|
| [김현국](https://github.com/014967) | [노유리](https://github.com/yforyuri) | [이서윤](https://github.com/seoyoon513) | [임수진](https://github.com/sujin-kk) |

## 3. Architecture 

> Clean Architecture + MVVM Pattern

<img width="324" alt="image" src="https://user-images.githubusercontent.com/85485290/191736907-c95f6fd2-ba75-47d6-9cf8-f499acb06a25.png">

- Presentation Layer
  - 화면에 애플리케이션 데이터를 표시하는 레이어입니다.
  - 화면에 데이터를 렌더링하는 UI 요소를 포함하고, 데이터를 보유하고 로직을 처리하는 ViewModel을 포함합니다.
  
- Data Layer
  - 데이터 노출 및 변경사항 등을 집중적으로 관리하는 레이어입니다.
  - Local Repository 및 DataSource가 존재합니다.
  - 앱의 전반적인 비즈니스 로직을 처리합니다.
  
- Fordering
```
🔖
.
├── data
│   ├── model
│   ├── repository
│   └── source
├── di
└── presentation
    ├── custom
    ├── model
    ├── ui
    └── uistates
```
### DataBase

| Timer Table||
|------|---|
|id(primary_key)|currentDuration(Int) |

| Location Table||||
|------|---|---|----|
|id(primary_key)|latitude(Float) | longitude(Float)| date(String)|
  
***

## 4. Feature & Screen ✨

### Permission Check

<img src="https://user-images.githubusercontent.com/110798031/191803139-44360ae1-3610-4366-82cf-744a5e502183.png">

|권한 승인|권한 거절|
|:----:|:----:|
|<img src="https://user-images.githubusercontent.com/110798031/191755875-a0a4e6ac-3532-4fae-8757-273fb80a3f15.gif" width="180" height="400">|<img src="https://user-images.githubusercontent.com/110798031/191757561-9b984447-b6c5-4496-8cb1-c5983e15d935.gif" width="180" height="400">|

* 최초 앱 실행 시 GPS on/off를 체크하고 on 상태에서 권한을 요청합니다.
* 권한을 거절하면 재요청 dialog를 띄우고 연이어 거절 시 설정 창에서 직접 권한을 수정합니다.
* 위치 권한을 허용하지 않을 경우 앱을 실행하면 다시 허용 안내 dialog를 띄우고 요청 로직이 재수행 됩니다.
* 권한을 허용하면 사용자 위치 추적이 시작되고 현재 사용자 위치에 마커가 표시됩니다.

***

### Alarm & Location Logic
<img src="https://user-images.githubusercontent.com/62296097/191752476-731287c3-cec0-40c1-8ec6-145f208008f8.png">


*** 

### TimerDialog 
<img src="https://user-images.githubusercontent.com/62296097/191736374-64727464-7938-444e-a4d3-033300aeff4a.jpeg" width="200px" with>

<p>
Timer Dialog에서는 알람 시간을 저장할 수 있습니다. 
설정한 알람은 Room Database의 "timer" table에 저장하게 됩니다.

EditText의 input 타입을 number로 설정하여 숫자만 입력받도록하였습니다.   
또한 시간은 최소 1분이상으로 설정하였습니다.   
0분을 입력시 Toast 메시지를 출력합니다.

</p>

###  시간 저장 로직

```kotlin
//Timer Repository
    fun getDuration(): Flow<Long> {
        return flow {
            val result = timerDataSource.getDuration()
            if (result != null) {
                emit(result)
            } else {
                emit(0)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun setDuration(duration: Long): Flow<Long> {
        return flow {
            emit(timerDataSource.setDuration(duration = duration))
        }.flowOn(Dispatchers.IO)
    }
```
<p>
기존에 설정한 알람 시간이 없다면 0을 반환하여 텍스트에 보여지도록하였습니다.                 

setDuration은 매개변수로 시간간격을 입력받아 timer 테이블에 시간을 저장합니다. 
</p>

### 알람 생성

```kotlin
if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
      alarmManager?.setExact(
        AlarmManager.RTC_WAKEUP,
        time * 1000 * 60,
        pendingIntent
        ) 
    } else {
        alarmManager?.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time * 1000 * 60,
            pendingIntent
        )
}
```
<p>

API 23 부터는 Doz 모드가 추가되어서 setExact()로도 정확한 시간을 보장할 수 없습니다. 

따라서 setExactAndAllowWhileIdle()을 이용하여 알람을 설정했습니다. 

</p>


***
### AlarmReceiver

```kotlin
override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (context != null) {
            when (intent?.action) {
                context.getString(R.string.setting_intent) -> {
                    createAlarm(context)
                    WorkManager.getInstance(context).enqueue(
                        OneTimeWorkRequestBuilder<LocationWorker>().build()
                    )
                }
                context.getString(R.string.setting_boot_intent) -> {
                    createAlarm(context)
                }
            }
        }
}
```
<p>
알람은 휴대폰이 꺼졌다 켜졌을 때 초기화되기 때문에, boot intent와 설정에서 생성한 intent들 수신했습니다.       

boot intent가 수신되면, locationRepository에서 현재 저장된 시간 간격을 받아와 알람을 다시 설정했습니다.

설정에서 생성한 intent가 수신되면, Workmanager에게 요청을 보냅니다.
</p>

*** 

### Workmanager

```kotlin
 override suspend fun doWork(): Result {
    return coroutineScope {
        // Permission 생략
        val manager: LocationManager = appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val currentLocation: Location? = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER) ?: manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        val latitude = currentLocation?.latitude
        val longitude = currentLocation?.longitude

        val now = System.currentTimeMillis()
        val date = Date(now)
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
        val time = simpleDateFormat.format(date)
        if (latitude != null && longitude != null) {
            val entity = LocationEntity(
                id = 0,
                latitude = latitude.toFloat(),
                longitude = longitude.toFloat(),
                date = time
            )
            locationRepository.saveLocation(location = entity)
        }
        Result.success()
    }
}
```
<p>
현재 Location을 가져올 때 GPS PROVIDER에서 제공받지 못하는 경우가 있어서  NETWORK_PROVIDER 를 추가했습니다.

위도와 경도를 입력받기 위해서는 ACCESS_BACKGROUND_LOCATION의 권한을 등록하고 사용자에게 권한을 요청하여,

위도를 항상 허용하도록 해야합니다.

위도와 경도가 null이 아닌 경우 locationRepository의 saveLocation을 호출하여 데이터베이스에 저장했습니다. 
</p>

---

### History

- 히스토리를 저장하고 다이얼로그로 띄워 확인하는 기능입니다.

![ezgif com-gif-maker (6)](https://user-images.githubusercontent.com/85485290/191758541-7ea72280-2319-47bf-9f20-70763bcd6d46.gif)


- DatePicker의 Calendar Instance는 앱을 사용하는 동안 한번만 생성하도록 하였고, mainViewModel.calendar로 언제든 참조 가능하게 하였습니다.
```kotlin
    var calendar: Calendar = Calendar.getInstance().apply {
        set(Calendar.MONTH, this.get(Calendar.MONTH))
        firstDayOfWeek = Calendar.MONDAY
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    }
```


- 히스토리를 띄우고 Date Picker에서 다른 날짜를 선택해 데이터를 reload 할 때 Flow의 ```collectLatest```로 요청이 들어왔을 때 최신의 데이터만 수집하도록 하였습니다.
```kotlin
    private fun updateHistoryList() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                kotlin.runCatching {
                    mainViewModel.getHistoryWithDate()
                }
                    .onSuccess {
                        mainViewModel.currentHistory.collectLatest {
                            historyListAdapter.submitList(it)
                        }
                    }
            }
        }
    }
```


- RecyclerView의 ListAdapter를 사용하여 이전 데이터와의 비교를 통해 바뀐 데이터 부분만 갱신하도록 하였습니다.
```kotlin
    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Location>() {
            override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
                return oldItem == newItem
            }
        }
    }
```
---

### Reverse geocoding

- 현재 위치 좌표값을 주소로 반환하는 역 지오코딩입니다.

<img src="https://user-images.githubusercontent.com/51072429/191809014-b2ec1cba-3c4e-48b7-9738-93aa7be88de0.GIF" width="250" />

- MainActivity에서 현재 위치 추적이 시작될 때 현재 위치 위도와 경도 값을 반환합니다.
```kotlin
    private fun startTracking(): Location { 
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading

        val lm: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val userNowLocation: Location? = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) 
        // 위도 , 경도
        val uLatitude = userNowLocation?.latitude
        val uLongitude = userNowLocation?.longitude
        val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude!!, uLongitude!!)
          ...
        return userNowLocation
    }
```

- 구글에서 제공하는 Geocoder 클래스의 getFromLocation 메서드로 반환된 주소 리스트는 getAddressLine 메서드를 통해 상세 주소를 반환합니다.
```kotlin
    private fun getAddress(): String? {
        val userLocation: Location? = startTracking()
        var userAddress: String? = null

        if (userLocation != null) {
            val latitude = userLocation.latitude
            val longitude = userLocation.longitude

            val mGeoCoder = Geocoder(this, Locale.KOREAN)
            var currentAddress: List<Address>? = null
            try {
                currentAddress = mGeoCoder.getFromLocation(
                    latitude,
                    longitude,
                    1
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (currentAddress != null) {
                userAddress = currentAddress[0].getAddressLine(0)
            }
        } else {
            userAddress = getString(R.string.check_gps_text)
        }
        return userAddress
    }
```

- 주소 버튼을 클릭하면 dialog가 호출되고 상세 주소를 매개변수로 전달해서 dialog를 통해 보여줍니다.
```kotlin
    binding.mainAddressBtn.setOnClickListener {
        val address = getAddress().toString().substring(5)
        AddressFragmentDialog(address).show(
            supportFragmentManager,
            "AddressFragmentDialog"
        )
    }
```

---
## 5. Technology Stack 🛠
- Tools : Android Studio Dolphin
- Language : Kotlin
- Architecture Pattern : MVVM Pattern
- Android Architecture Components(AAC)
  - Flow
  - ViewModel
  - Coroutine
  - View Binding
- HILT
- ROOM
- WORKER MANAGER
- KAKAO MAP
- TIMBER 

---

## 6. Git Convention

### Commit Message
 
```
[type] message
```

- Commit Type
  -  ```feat``` : 새로운 기능 추가
  -  ```fix``` : 버그 수정
  -  ```docs``` : 문서 수정
  -  ```refactor``` : 코드 리팩토링
  -  ```chore``` : 빌드 업무 수정 등 로직에 큰 변화 없는 업무

### Branch Strategy

<img width="400" alt="image" src="https://user-images.githubusercontent.com/85485290/191769024-ba45dc94-842c-4d1c-a28b-bc3617708ffb.png">

- Branch Type
  - ```develop``` : 개발 진행 중일때는 develop에 merge 한다.
  - ```feature``` : 기능 개발 브랜치
  - ```fix``` : 버그 및 기능 수정 브랜치
  - ```refactor``` : 리팩토링 브랜치
  - ```hotfix``` : main의 버그가 있으면 hotfix를 이용해 main에 merge 한다.
  - ```main``` : 금요일 코드 제출을 위한 최종 브랜치
