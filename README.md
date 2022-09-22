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
