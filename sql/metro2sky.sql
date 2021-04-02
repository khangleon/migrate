alter table partner add column external_uuid varchar(100);
alter table imaging_result add column external_uuid varchar(100);
alter table item add column external_uuid varchar(100);
alter table imaging_image add column external_uuid varchar(100);
alter table imaging_sub add column external_uuid varchar(100);
alter table imaging_template add column external_uuid varchar(100);
alter table imaging_sub add column remind_date bigint;

update report_template set company_id = 211, branch_id = 212;

alter table item_category add column screen varchar(100);
alter table item_category add column worklist smallint default 0;
alter table item_group add column report smallint default 0;

delete from company;
delete from branch;
delete from users;
delete from user_branch;
delete from user_department;
delete from user_last_department;

delete from partner;
delete from medic;
delete from patient;

delete from item;
delete from item_price;
delete from imaging_result;
delete from imaging_label;
delete from imaging_sub;

delete from imaging_image;

delete from department;

INSERT INTO company(id,partner_id,search_text,code,name,nick_name,business_name,locale,locale_foreign,time_zone,diff_hour,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('211','211','PHONG KHAM 91 PHAM HUU CHI','91PHC','PHÒNG KHÁM 91 PHẠM HƯU CHÍ','PHÒNG KHÁM 91 PHẠM HƯU CHÍ','PK 91 PHẠM HỮU CHÍ','vi-VN','en-US','GMT+07:03','7','0','0','0','0','0','1');
INSERT INTO branch(id,company_id,search_text,code,name,nick_name,business_name,locale,locale_foreign,time_zone,diff_hour,headquarter,address,en_business_name,country_id,state_id,province_id,district_id,ward_id,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('212','211','PHONG KHAM 91 PHAM HUU CHI','91PHC','PHÒNG KHÁM 91 PHẠM HỮU CHÍ','PHÒNG KHÁM 91 PHẠM HƯU CHÍ','PK 91 PHẠM HỮU CHÍ','vi-VN','en-US','GMT+07:00','7','1','','','704','0','1','0','0','0','0','0','0','0','0','1');

INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('013','212','4757110212018','PK PHONG KHAM','PK','Phòng khám','Phòng khám','','','','','1','0','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('023','212','4757110212018','CC CAP CUU','CC','Cấp cứu','Cấp cứu','','','','','2','0','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('033','212','4757110212018','KSK KHAM SUC KHOE','KSK','Khám sức khỏe','Khám sức khỏe','','','','','3','0','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('043','212','4757110212018','VAC VACCINE','VAC','Vaccine','Vaccine','','','','','4','0','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('053','212','4757110212018','NS NOI SOI','NS','Nội soi','Nội soi','','','','','5','0','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('063','212','4757110212018','TMH TAI MUI HONG','TMH','Tai - Mũi - Họng','Tai - Mũi - Họng','','','','','6','0','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('073','212','4757110212018','PhuK PHU KHOA','PhuK','Phụ khoa','Phụ khoa','','','','','7','0','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('083','212','4757110212018','NamK NAM KHO','NamK','Nam khoa','Nam khoa','','','','','8','0','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('093','212','4757110212018','NhanK NHAN KHOA','NhanK','Nhãn khoa','Nhãn khoa','','','','','9','0','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('103','212','4757110212018','RHM RAN HAM MAT','RHM','Răng - Hàm - Mặt','Răng - Hàm - Mặt','','','','','10','0','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('113','212','4757110212018','SanK SAN KHOA','SanK','Sản khoa','Sản khoa','','','','','11','0','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('123','212','4757110212018','NhiK NHI KHOA','NhiK','Nhi khoa','Nhi khoa','','','','','12','0','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('143','212','4757110212018','HMVS HIEM MUON VO SINH','HMVS','Hiếm muộn - Vô sinh','Hiếm muộn - Vô sinh','','','','','13','0','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('153','212','4757110212018','TM TIM MACH','TM','Tim mạch','Tim mạch','','','','','14','0','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('713','212','4757110212018','CDHA CHAN DOAN HINH ANH','CDHA','Chẩn đoán hình ảnh','Chẩn đoán hình ảnh','','','','','15','0','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('723','212','4757110212018','TDCN THAM DO CHUC NANG','TDCN','Thăm dò chức năng','Thăm dò chức năng','','','','','16','0','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('743','212','4757110212018','XN XET NGHIEM','XN','Xét nghiệm','Xét nghiệm','','','','','17','0','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('773','212','4757110212018','GPB GIAI PHAU BENH','GPB','Giải phẫu bệnh - Tế bào bệnh học','Giải phẫu bệnh - Tế bào bệnh học','','','','','18','0','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('853','212','4757110212018','QT QUAY THUOC','QT','Quầy thuốc','Quầy thuốc','','','','','19','0','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('863','212','4757110212018','KD KHO DUOC','KD','Kho dược - Vật tư','Kho dược - Vật tư','','','','','20','0','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('883','212','4757110212018','TTB TRANG THIET BI _ DUNG CU Y TE','TTBDC','Trang thiết bị - Dụng cụ y tế','Trang thiết bị - Dụng cụ y tế','','','','','21','0','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('893','212','4757110212018','DN DIEN NUOC','DN','Điện nước','Điện nước','','','','','22','1','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('903','212','4757110212018','IT PHONG IT','IT','Phòng IT','Phòng IT','','','','','23','0','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('913','212','4757110212018','MARKETING','MARKETING','Marketing','Marketing','','','','','24','1','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('923','212','4757110212018','BH BAN HANG','BH','Bán hàng','Bán hàng','','','','','25','1','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('933','212','4757110212018','MH MUA HANG','MH','Mua hàng','Mua hàng','','','','','26','1','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('943','212','4757110212018','TT TIEP TAN','TT','Tiếp tân','Tiếp tân','','','','','27','0','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('953','212','4757110212018','TN THU NGAN','TN','Thu ngân','Thu ngân','','','','','28','0','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('963','212','4757110212018','BH BAO HIEM','BH','Bảo hiểm','Bảo hiểm','','','','','29','0','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('973','212','4757110212018','KT KE TOAN','KT','Kế toán','Kế toán','','','','','30','0','0','0','0','0','1');
INSERT INTO department(id,branch_id,partner_id,search_text,code,name,nick_name,business_name,representative,representative_title,email_address,sort,disabled,created_by,created_date,updated_by,updated_date,version) VALUES('983','212','4757110212018','HCNS HANH CHINH NHAN SU','HCNS','Hành chính - Nhân Sự','Hành chính - Nhân Sự','','','','','31','0','0','0','0','0','1');


update department set branch_id = 212;

select * from users where id = 108610212018;

select * from partner where user_id = 108610212018;


select * from user_department where 
	branch_id = 212
	and user_id = 108610212018
	;
	
	select * from department where id = 6913;



delete from user_department ;

delete from user_last_department ;

select * from public."EndoscopicMedicalRecord" 
where EndoscopicMedicalRecord_id = '00005ec7-28f1-437d-b1ea-9372b23eedc2'; 

select * from public."EndoscopicMedicalRecord" 
where EndoscopicMedicalRecord_id = '00005ec7-28f1-437d-b1ea-9372b23eedc2';

select * from public."EndoscopicMedicalRecordItem" 
where EndoscopicMedicalRecord_id = '00005ec7-28f1-437d-b1ea-9372b23eedc2';

select * from public."Patients" where patients_id = 'cd6b6671-7811-428f-8c68-915015c15671';

SELECT a.* FROM public."SystemAccount"  a
	join  public."Doctor" d on a.systemaccount_id = d.account_id
	where a.user_name = 'hdqd';
	
update public."SystemAccount" set password = '1+iye2s0iro=' ,
	isrole = 0,
	rolelevel = 0, 
	role_id = '279c79e2-fc5f-495c-8615-96c24f76a712'
where systemaccount_id = '279c79e2-fc5f-495c-8615-96c24f76a712';

b4qxnGAw0pX79AACvye4Nw==
update public."SystemAccount" set password = '1+iye2s0iro=' where systemaccount_id = 'ed9fd2b1-3c99-4248-8587-aefba44a7915';
	
select * from imaging_result where company_id = 211;

update imaging_result set screen = 'img211', search_text = '' where company_id = 211;

select * from public."MediaInfo" 
where EndoscopicMedicalRecord_id = '00005ec7-28f1-437d-b1ea-9372b23eedc2';

select created_date,to_timestamp(created_date/1000) from imaging_result 
		where company_id = 211
		and created_by = 4757310212018
		and status between 5 and 8
		and created_date between 1388682000000 and 1541782799999 ;
		
select to_timestamp(1349024400000/1000);

select * from item where company_id = 211;

delete from item where company_id = 211;

update item set 
	search_text = 'NS',
	reg_code = '',
	cpt_code = '',
	barcode = '',
	item_type = 1,
	av_indication = 1, 
	screen = 'img211'
	where company_id = 211;
	select * from "EndoscopicMedicalRecord" where deleted = 0
		and code = '20180910-62116'
		--and clo_testresult not in (1,2)
		--and biopsyresult != ''
;

select em.* from "EndoscopicMedicalRecordItem" em
		join "EndoscopicProcedure" ep on em.procedurecode = ep.code
where em.endoscopicmedicalrecord_id = '34cf5ba7-a799-4d5d-a387-28d758200411';

select * from imaging_result where created_date is not null;
update imaging_result set created_date = performance_date;
delete from imaging_template;
