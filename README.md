# Petmilier
반려동물 관리 어플리케이션

# 🐾 프로젝트 소개
반려동물을 키우는 인구가 증가하고 있지만, 아직 여러 동물을 대상으로한 어플은 없다.
동물의 종과 상관없이 반려동물을 키우는 사용자들이 서로 정보를 공유할 수 있는 프로젝트를 목표로 진행하였다.

# 📅 개발 기간
2022.08.29 ~ 2022.11.18

# 🧑‍🤝‍🧑 팀원
☑ 팀원1: 유은경 - 팀장, 게시판페이지, 메모장페이지<br>
☑ 팀원2: 노명진 - UI 디자인, 지도페이지<br>
✅ 팀원3: 이도희(<b>본인</b>) - 로그인 및 회원가입 페이지, 메인페이지, 설정페이지

# ⚙️ 개발 환경
![Firebase](https://img.shields.io/badge/Firebase-039BE5?style=for-the-badge&logo=Firebase&logoColor=white) ![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84.svg?style=for-the-badge&logo=android-studio&logoColor=white) ![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)

# 📋 기능 소개
<h3>✅ 나의 제작 페이지</h3>
☑ 팀원의 제작 페이지
<hr>
<h3>✅ 안내 페이지</h3>

- 로딩화면
- 로그인 버튼 클릭 시 로그인 페이지 이동
- 회원가입 버튼 클릭 시 회원가입 페이지 이동

<img src="https://github.com/DH083/Focho/assets/147012079/1060d6be-20bc-4429-9178-980664f85180.png"  width="300" height="100%"/> <img src="https://github.com/DH083/Focho/assets/147012079/a5b47fb1-752b-4aea-b993-d131e5ea441a.png"  width="300" height="100%"/>
<hr>

<h3>✅ 회원가입 페이지</h3>

 - 비밀번호, 이메일 형식 확인
 - 회원가입 버튼을 클릭했을 때 공란이 있거나 유효성검사가 완료되지 않은 칸이 있다면 회원가입 실패
 - 공란이 없거나, 모든 유효성검사가 완료되었다면 DB에 회원 추가, 프로필 등록 페이지 이동

<img src="https://github.com/DH083/Focho/assets/147012079/3b17ecfa-115f-48f3-b0d7-131bd869482b.png"  width="300" height="100%"/>
<hr>

<h3>✅ 반려동물 등록 페이지</h3>

 - 이미지 변경 버튼 클릭시 기기 갤러리 연동
 - 프로필 수정 클릭시 반려동물 정보 등록 페이지 이동
 - 생일, 만난 날에 선택버튼 클릭시 달력 나타냄
 - 프로필은 추후 등록하거나 수정 가능

<img src="https://github.com/DH083/Focho/assets/147012079/94b9367f-05d4-469f-9b69-1c215adea074.png"  width="300" height="100%"/> <img src="https://github.com/DH083/Focho/assets/147012079/1e9eed0f-1cd6-4af4-9c0e-f4a2e8f86630.png"  width="300" height="100%"/> <img src="https://github.com/DH083/Focho/assets/147012079/afff26a6-93b1-4e74-99a6-c84c43e08d3f.png"  width="300" height="100%"/>
<hr>

<h3>✅ 로그인 페이지</h3>

 - 로그인 버튼 클릭 시 유저 로그인, 메인화면 이동
 - 로그인 버튼을 클릭했을 때 공란이 있으면 로그인 실패
 - 입력한 아이디와 비밀번호가 DB와 비교했을 때 일치하지 않을 시 로그인 실패

<img src="https://github.com/DH083/Focho/assets/147012079/b57f7d79-9dee-47eb-9dca-b6bd06964097.png"  width="300" height="100%"/>
<hr>

<h3>✅ 메인 페이지</h3>

- 사용자가 등록한 반려동물의 사진과 이름 불러오기
- 우측 상단에 반려동물과의 디데이 계산
- 하단 네비게이션 바를 통해 메인페이지, 게시판 페이지, 지도 페이지, 메모장 페이지, 설정 페이지로 이동

<img src="https://github.com/DH083/Focho/assets/147012079/d81cd65c-b918-47d7-b2d9-f96ccc4151d7.png"  width="300" height="100%"/>
<hr>

<h3>☑ 게시판 페이지</h3>
 - 게시판 목록 불러오기
 - 게시판 선택시 해당 게시판으로 이동, 해당 게시판에 작성된 글 목록 가져오기

<img src="https://github.com/DH083/Focho/assets/147012079/dd99406b-b8ae-4f84-b643-930ed750cd5c.png"  width="300" height="100%"/>
<hr>

<h3>☑ 지도 페이지</h3>
 - 주변 병원 찾기 버튼 클릭시 지도로 이동 (구글 지도 연동)
 - 근처 동물병원 주황핀으로 나타내기
 - 주황핀 선택시 동물병원 정보 나타내기

<img src="https://github.com/DH083/Focho/assets/147012079/78c2ab3c-7b76-4e8d-a036-cdb82c223f26.png"  width="300" height="100%"/> <img src="https://github.com/DH083/Focho/assets/147012079/b247fb79-7d33-4fe4-ad9e-f9f92e3898b6.png"  width="300" height="100%"/>
<hr>

<h3>☑ 메모 페이지</h3>

<img src=".png"  width="300" height="100%"/>
<hr>

<h3>✅ 설정 페이지</h3>
 - 반려동물 이미지를 클릭 시 반려동물 정수 수정 페이지로 이동
 - 문의하기 버튼 클릭 시 기기의 메일 앱 목록 불러옴, 메일 수신자에 수신자 자동 입력
 - 로그아웃 버튼 클릭 시 기기 로그아웃, 안내 페이지로 이동

<img src="https://github.com/DH083/Focho/assets/147012079/5bd0f580-8cc6-4141-8869-08b6654e0366.png"  width="300" height="100%"/> <img src="https://github.com/DH083/Focho/assets/147012079/f0f7e997-e936-44a0-be53-e4a3f890cb62.png"  width="300" height="100%"/>
<hr>
