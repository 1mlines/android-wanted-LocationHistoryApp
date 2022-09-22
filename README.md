# 원티드 프리온보딩 안드로이드

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
  
  
***

# Alarm & Location Logic
<img src="https://user-images.githubusercontent.com/62296097/191752476-731287c3-cec0-40c1-8ec6-145f208008f8.png">


*** 

## TimerDialog 
<img src="https://user-images.githubusercontent.com/62296097/191736374-64727464-7938-444e-a4d3-033300aeff4a.jpeg" width="200px">

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
## AlarmReceiver

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

## Workmanager

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

