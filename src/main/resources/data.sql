--CREATE MEMBER TABLE
create table tbl_member(
userid VARCHAR(50) NOT NULL,
userpwd VARCHAR(50) NOT NULL,
email VARCHAR(50) NOT NULL,
PRIMARY KEY(userid)
);

--test case
insert into tbl_member(userid,userpwd,email) values("01065512046","1234","tlaabs@naver.com");
insert into tbl_member(userid,userpwd,email) values("01023945707","5678","csm1615@naver.com");