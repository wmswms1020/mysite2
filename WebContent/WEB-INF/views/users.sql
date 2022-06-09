drop table users;
drop sequence seq_users_no;

create table users (
    no number,
    id varchar2(20) unique not null,
    password varchar2(20) not null,
    name varchar2(20),
    gender varchar2(10),
    primary key(no)
);

create sequence seq_users_no
increment by 1
start with 1
nocache;

insert into users
values(seq_users_no.nextval,'wmswms1020','aszx745','소준호','male');

select * from users;

create table board(
	no number,
	title varchar2(500) not null,
	content varchar(4000),
	hit number(0),
	req_date date not null,
	user_no number not null,
	primary key(no),
	CONSTRAINT board_fk foreign key (user_no)
    references users(no)
);

create sequence seq_board_no
increment by 1
start with 1
nocache;
  
.