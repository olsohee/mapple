# Mapple (마이 플레이스, 마플)

## 프로젝트 소개

### 참여 인원

1명 (개인 프로젝트)

### 프로젝트 기간

2024.01 ~

### 프로젝트 설명

방문했던 장소에 대한 리뷰 글을 작성할 수 있으며, 다음에 방문하고 싶은 장소를 친구들과 모임을 생성하여 함께 기록할 수 있는 서비스입니다.

#### 사용자 관련 기능

- 회원가입
- 로그인 (기본 로그인, 카카오/네이버 로그인)

#### 리뷰 관련 기능

- 리뷰 글 CRUD
- 좋아요 등록 / 취소

#### 친구 관련 기능

- 친구 요청 / 수락 / 거절

#### 모임, 플레이스 관련 기능

- 친구들과의 모임 생성
- 다음에 갈 장소(플레이스) CRUD

### 실행 예시

더 자세한 예시와 설명은 API 문서 참고 (https://documenter.getpostman.com/view/25391549/2s9Yytg11m)

#### 로그인 시 응답

```
{
    "message": "사용자 로그인 성공",
    "data": {
        "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMUBuYXZlci5jb20iLCJpYXQiOjE3MDgyMzkxMTEsImV4cCI6MTcwODI0MDkxMX0.sSOsmv3rSNBv3kkxzUMGHcERKIa4K2j1oBsw2kuyZn4",
        "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMUBuYXZlci5jb20iLCJpYXQiOjE3MDgyMzkxMTEsImV4cCI6MTcwODI0MjcxMX0.vbZMnLnzSd7rSqLYihoWT_r_aogEtQO6hZAz4EwcecE"
    }
}
```

#### 리뷰 리스트 조회 시 응답

```
{
    "message": "전체 리뷰 리스트 조회 성공",
    "data": {
        "content": [
            {
                "reviewId": 999,
                "username": "user999",
                "placeName": "zeuhxqzefl",
                "rating": "FIVE",
                "likeCount": 0,
                "createdAt": "2024-02-19T14:20:42.465291",
                "updatedAt": "2024-02-19T14:20:42.465291"
            },
            {
                "reviewId": 996,
                "username": "user996",
                "placeName": "kzspjvsouw",
                "rating": "FIVE",
                "likeCount": 0,
                "createdAt": "2024-02-19T14:20:42.462833",
                "updatedAt": "2024-02-19T14:20:42.462833"
            },
            {
                "reviewId": 993,
                "username": "user993",
                "placeName": "gnzgcdewdg",
                "rating": "FIVE",
                "likeCount": 0,
                "createdAt": "2024-02-19T14:20:42.460571",
                "updatedAt": "2024-02-19T14:20:42.460571"
            },
            {
                "reviewId": 990,
                "username": "user990",
                "placeName": "vhiqjgsmsn",
                "rating": "FIVE",
                "likeCount": 0,
                "createdAt": "2024-02-19T14:20:42.458319",
                "updatedAt": "2024-02-19T14:20:42.458319"
            },
            {
                "reviewId": 987,
                "username": "user987",
                "placeName": "ryvowqfobr",
                "rating": "FIVE",
                "likeCount": 0,
                "createdAt": "2024-02-19T14:20:42.455281",
                "updatedAt": "2024-02-19T14:20:42.455281"
            }
        ],
        "pageable": {
            "pageNumber": 0,
            "pageSize": 5,
            "sort": {
                "empty": false,
                "sorted": true,
                "unsorted": false
            },
            "offset": 0,
            "paged": true,
            "unpaged": false
        },
        "last": false,
        "totalPages": 67,
        "totalElements": 334,
        "first": true,
        "size": 5,
        "number": 0,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "numberOfElements": 5,
        "empty": false
    }
}
```

#### 친구 요청 성공 시 응답

```
{
    "message": "친구 요청 성공",
    "data": {
        "fromUsername": "user1",
        "toUsername": "user2",
        "requestStatus": "REQUEST"
    }
}
```

## 기술 스택

<img src="https://img.shields.io/badge/Spring-6DB33F?style=plastic&logo=spring&logoColor=white"> <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=plastic&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=plastic&logo=springsecurity&logoColor=white"> <img src="https://img.shields.io/badge/OAuth-EB5424?style=plastic&logo=auth0&logoColor=white"> </br>

<img src="https://img.shields.io/badge/Hibernate-59666C?style=plastic&logo=hibernate&logoColor=white"> <img src="https://img.shields.io/badge/Querydsl-1E8CBE?style=plastic&logoColor=white"> <img src="https://img.shields.io/badge/Redis-DC382D?style=plastic&logo=redis&logoColor=white"> <img src="https://img.shields.io/badge/MySQL-4479A1?style=plastic&logo=mysql&logoColor=white">

## ERD 
![image](https://github.com/olsohee/mapple/assets/108605017/097e21dc-30cd-4d44-bdbf-d5cf1780695a)
