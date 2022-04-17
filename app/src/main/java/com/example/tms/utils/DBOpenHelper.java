package com.example.tms.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    public DBOpenHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //学员信息
        String sql_student = "create table student(" +
                "id integer primary key AUTOINCREMENT not null ," +
                "name varchar(20) not null," +
                "sex varchar(10) not null," +
                "age int check(age<100 and age>0)," +
                "department varchar(20) ," +
                "phone int ," +
                "company varchar(20) default '无')";
        //学员——登陆密码表
        String sql_student_load = "create table account(" +
                "account int primary Key not null ," +
                "role int," +
                "password varchar(20))";
        String sql_admin_insert = "insert into account values(10000,3,'e10adc3949ba59abbe56e057f20f883e')";
        //触发器
        String sql_ctrigger = "CREATE TRIGGER account_s_add AFTER INSERT ON student  " +
                "BEGIN INSERT INTO account(account,role,password) VALUES (new.id,1,'e10adc3949ba59abbe56e057f20f883e'); END;";
        String sql_trigger = "CREATE TRIGGER account_s_delete BEFORE DELETE ON student BEGIN DELETE  FROM account WHERE account =old.id ;END ; ";
        String sql_trigger2 = "CREATE TRIGGER account_s_change " +
                " AFTER UPDATE ON student BEGIN UPDATE  account SET ACCOUNT = NEW.id WHERE ACCOUNT = OLD.ID; END;";
        db.execSQL(sql_student_load);
        db.execSQL(sql_admin_insert);
        db.execSQL(sql_student);
        db.execSQL(sql_ctrigger);
        db.execSQL(sql_trigger);
        db.execSQL(sql_trigger2);
        //讲师个人信息表
        String sql_lecturer = "create table lecturer(" +
                "id integer primary key not null," +
                "name varchar(20)," +
                "sex  varchar(20) check(sex = '男' or sex = '女')," +
                "age int check(age>0 and age<100)," +
                "level varchar(20)," +
                "phone int ," +
                "company varchar(20))";

        //触发器
        String sql_ttrigger = "CREATE TRIGGER account_l_add AFTER INSERT ON lecturer  " +
                "BEGIN INSERT INTO account(account,role,password) VALUES (new.id,2,'e10adc3949ba59abbe56e057f20f883e'); END;";
        String sql_dttrigger = "CREATE TRIGGER account_l_delete " +
                " BEFORE DELETE ON lecturer BEGIN DELETE FROM account WHERE ACCOUNT = OLD.ID; END;";
        String sql_dttrigger1 = "CREATE TRIGGER account_l_change " +
                " AFTER UPDATE ON lecturer BEGIN UPDATE  account SET ACCOUNT = NEW.id WHERE ACCOUNT = OLD.ID; END;";
        db.execSQL(sql_lecturer);
        db.execSQL(sql_ttrigger);
        db.execSQL(sql_dttrigger);
        db.execSQL(sql_dttrigger1);
        //学员选课信息表
        String sql_student_course = "create table student_course(" +
                "student_id integer ," +
                "course_name varchar(20)," +
                "lecturer_name varchar(10)," +
                "score int, " +
                "FOREIGN KEY(student_id) REFERENCES student(id)," +
                "FOREIGN KEY(course_name) REFERENCES course(course_name)," +
                "FOREIGN KEY(lecturer_name) REFERENCES course(lecturer_name)" +
                ")";

        //课程信息表
        String sql_course = "create table course(" +
                "course_id integer not null primary key AUTOINCREMENT," +
                "lecturer_name varchar(20) not null," +
                "course_name varchar(20) not null," +
                "course_weight SMALLINT ," +
                "course_time varchar(20)," +
                "course_period varchar(20), " +
                "course_img varchar(200), " +
                "FOREIGN KEY(lecturer_name) REFERENCES lecturer(name))";
        db.execSQL(sql_course);
        db.execSQL(sql_student_course);
        //留言信息存储表
        String sql_liuyan = "create table message(" +
                "student_id int ," +
                "message text)";
        db.execSQL(sql_liuyan);
        //讨论表
        String sql_discuss = "create table discuss(" +
                "discuss_id integer primary key AUTOINCREMENT not null," +
                "course_id integer not null," +
                "student_id integer not null," +
                "discuss_content varchar(200)," +
                "foreign key(course_id) references course(course_id)," +
                "foreign key(student_id) references student(id))";
        db.execSQL(sql_discuss);
        String discuss1 = "insert into discuss values(null,2,100,'1啊吧啊吧')";
        String discuss2 = "insert into discuss values(null,2,101,'2啊吧啊吧')";
        String discuss3 = "insert into discuss values(null,2,102,'3啊吧啊吧')";
        String discuss4 = "insert into discuss values(null,2,100,'4啊吧啊吧')";
        db.execSQL(discuss1);
        db.execSQL(discuss2);
        db.execSQL(discuss3);
        db.execSQL(discuss4);

        //签到表
        String sql_checkin = "create table checkin(" +
                "checkin_id integer primary key AUTOINCREMENT not null ," +
                "course_id integer not null," +
                "student_id integer not null," +
                "checkin_date timestamp default CURRENT_TIMESTAMP," +
                "foreign key(course_id) references course(course_id)," +
                "foreign key(student_id) references student(id))";
        db.execSQL(sql_checkin);
        String checkin1 = "insert into checkin values(null,2,100,'2022-01-01 00:00:00')";
        String checkin2 = "insert into checkin values(null,2,100,'2022-01-01 00:00:00')";
        String checkin3 = "insert into checkin values(null,2,100,'2022-01-01 00:00:00')";
        String checkin4 = "insert into checkin values(null,2,100,'2022-01-01 00:00:00')";
        String checkin5 = "insert into checkin values(null,2,100,'2022-01-01 00:00:00')";
        String checkin6 = "insert into checkin values(null,2,100,'2022-01-01 00:00:00')";
        db.execSQL(checkin1);
        db.execSQL(checkin2);
        db.execSQL(checkin3);
        db.execSQL(checkin4);
        db.execSQL(checkin5);
        db.execSQL(checkin6);


        //提问表
        String sql_ask = "create table ask (" +
                "ask_id integer primary key AUTOINCREMENT not null," +
                "course_id integer not null," +
                "student_id integer not null," +
                "ask varchar(200)," +
                "answer varchar(200)," +
                "foreign key(course_id) references course(course_id)," +
                "foreign key(student_id) references student(student_id))";
        db.execSQL(sql_ask);
        String ask_insert1 = "insert into ask values(null,2,100,'1啊吧啊吧','1啊咧啊咧')";
        String ask_insert5 = "insert into ask values(null,2,100,'2啊吧啊吧','2啊咧啊咧')";
        String ask_insert2 = "insert into ask values(null,2,101,'3啊吧啊吧','3啊咧啊咧')";
        String ask_insert3 = "insert into ask values(null,1,100,'4啊吧啊吧','4啊咧啊咧')";
        String ask_insert4 = "insert into ask values(null,3,103,'5啊吧啊吧','5啊咧啊咧')";
        db.execSQL(ask_insert1);
        db.execSQL(ask_insert2);
        db.execSQL(ask_insert3);
        db.execSQL(ask_insert4);
        db.execSQL(ask_insert5);

        //个人资源配置表，比如更改图片之类的啦
        String personal_resource = "create table personal_resource(" +
                "id integer primary key not null ," +
                "person_img blob)";
        db.execSQL(personal_resource);
        //触发器
        String personal_resource_trigger = "CREATE TRIGGER personal_resource_trigger AFTER INSERT ON student " +
                "BEGIN INSERT INTO personal_resource(id,person_img) VALUES (new.id,null);END;";
        String personal_resource_trigger1 = "CREATE TRIGGER personal_resource_trigger_l AFTER INSERT ON lecturer " +
                "BEGIN INSERT INTO personal_resource(id,person_img) VALUES (new.id,null);END;";
        db.execSQL(personal_resource_trigger);
        db.execSQL(personal_resource_trigger1);
        //插入基本测试数据
        String sql_insert1 = "insert into course values('1','孙军','情商与您的日常工作',2,'周一上午','1','https://static.moxueyuan.com/uploads_v3/e/0/e0/img/2022/01/20/16426710193466.jpg_thumb_400.jpg')";
        String sql_insert2 = "insert into course values('2','赵彦辉','职场小白的思维进阶课',4,'周五上午','1','https://static.moxueyuan.com/uploads_v3/e/0/e0/img/2021/08/05/16281422993930.jpg_thumb_400.jpg')";
        String sql_insert3 = "insert into course values('3','孙军','分析问题的方法和工具',3,'周三上午','1','https://static.moxueyuan.com/uploads_v3/e/0/e0/img/2022/01/20/16426727234068.jpg_thumb_400.jpg')";
        String sql_insert4 = "insert into course values('4','杨利群','了解并更好地控制情绪',2,'周一晚上','1','https://static.moxueyuan.com/uploads_v3/e/0/e0/img/2021/12/16/16396452148012.jpg_thumb_400.jpg')";
        String sql_insert5 = "insert into course values('5','杨利群','职业倦怠应对',2,'周三下午','1','https://static.moxueyuan.com/uploads_v3/e/0/e0/img/2021/08/12/1628779812402.png_thumb_400.png')";
        String sql_insert6 = "insert into course values('6','杨红云','10招玩转Excel主流技能',4.5,'周五晚上','1','https://static.moxueyuan.com/uploads_v3/e/0/e0/img/2021/09/28/16327919753636.jpg_thumb_400.jpg')";
        db.execSQL(sql_insert1);
        db.execSQL(sql_insert2);
        db.execSQL(sql_insert3);
        db.execSQL(sql_insert4);
        db.execSQL(sql_insert5);
        db.execSQL(sql_insert6);
        String student_course_insert = "INSERT INTO student_course VALUES (100, '职场小白的思维进阶课', '赵彦辉', NULL) ";
        String student_course_insert1 = "INSERT INTO student_course VALUES (100, '分析问题的方法和工具', '孙军', NULL)";
        db.execSQL(student_course_insert);
        db.execSQL(student_course_insert1);
        //插入基本测试数据
        String sql_p = "insert into student values(100,'问庆昆','男',19,'部门1',188330,'xx公司1')";
        String sql_p1 = "insert into student values(101,'刘晓东','女',20,'部门2',188330,'xx公司2')";
        String sql_p2 = "insert into student values(102,'台x','男',20,'部门1',188330,'xx公司1')";
        String sql_p3 = "insert into student values(103,'王x','男',20,'部门2',188330,'xx公司2')";
        String sql_p4 = "insert into student values(104,'霍xx','男',20,'部门1',188330,'xx公司1')";
        String sql_tp1 = "insert into lecturer values(1000,'孙军','男',20,'超高级讲师',188330,'xx公司1')";
        String sql_tp = "insert into lecturer values(1001,'杨利群','男',19,'高级讲师',188330,'xx公司2')";
        String sql_tp2 = "insert into lecturer values(1002,'杨红云','女',19,'高级讲师',188330,'xx公司2')";
        db.execSQL(sql_p);
        db.execSQL(sql_tp);
        db.execSQL(sql_p1);
        db.execSQL(sql_tp2);
        db.execSQL(sql_p2);
        db.execSQL(sql_p3);
        db.execSQL(sql_p4);
        db.execSQL(sql_tp1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
