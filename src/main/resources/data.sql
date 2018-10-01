--유저생성
create user 'sluser'@'%' identified by '0000';

--유저 권한
GRANT ALL PRIVILEGES ON slu.* TO 'sluser'@'%';

--CREATE slu Database
CREATE DATABASE slu DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

--CREATE MEMBER TABLE
create table tbl_member(
userid VARCHAR(50) NOT NULL,
userpwd VARCHAR(50) NOT NULL,
email VARCHAR(50) NOT NULL,
state VARCHAR(50),
emotion VARCHAR(50),
username VARCHAR(50) NOT NULL,
PRIMARY KEY(userid)
);

--test case
insert into tbl_member(userid,userpwd,email,state,username) values("01065512046","1234","tlaabs@naver.com","행복","심세용");
insert into tbl_member(userid,userpwd,email,state,username) values("01023945707","5678","csm1615@naver.com","감동","소나무");
