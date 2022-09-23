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
