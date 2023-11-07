USE MASTER
IF EXISTS (SELECT * FROM SYS.DATABASES WHERE NAME = 'De10_1')
	DROP DATABASE De10_1
GO
CREATE DATABASE De10_1
GO
USE De10_1
GO

--Bảng Users: Lưu trữ thông tin người dùng.
CREATE TABLE Users (
    user_id INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email_quota INT DEFAULT 100, -- Dung lượng hộp thư của người dùng (MB)
    CONSTRAINT unique_username UNIQUE (username)
);
--Bảng Emails: Lưu trữ thông tin về email.
CREATE TABLE Emails (
    email_id INT IDENTITY(1,1) PRIMARY KEY,
    subject VARCHAR(255),
    body TEXT,
    sender_id INT NOT NULL, -- Người gửi email
    timestamp DATETIME NOT NULL,
    is_read INT DEFAULT 0, -- 0 là chưa đọc, 1 là đã đọc
    is_spam INT DEFAULT 0, -- 0 là không phải spam, 1 là spam
    is_deleted INT DEFAULT 0, -- 0 là chưa xóa, 1 là đã xóa
    CONSTRAINT fk_sender FOREIGN KEY (sender_id) REFERENCES Users(user_id)
);
--Bảng Recipients: Liên kết người dùng với các email mà họ nhận được.
CREATE TABLE Recipients (
    recipient_id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL, -- Người nhận email
    email_id INT NOT NULL, -- Email đã nhận
    is_cc INT DEFAULT 0, -- Có phải là CC không?
    is_bcc INT DEFAULT 0, -- Có phải là BCC không?
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES Users(user_id),
    CONSTRAINT fk_email FOREIGN KEY (email_id) REFERENCES Emails(email_id)
);
--Bảng EmailAttachments: Lưu trữ thông tin về tệp đính kèm trong email.
CREATE TABLE EmailAttachments (
    attachment_id INT IDENTITY(1,1) PRIMARY KEY,
    email_id INT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_data VARBINARY(MAX), -- Sử dụng VARBINARY(MAX) thay cho BLOB
    CONSTRAINT fk_email_attachment FOREIGN KEY (email_id) REFERENCES Emails(email_id)
);

go
--ALTER  PROCEDURE sp_InsertEmail
--    @subject NVARCHAR(255),
--    @body NVARCHAR(MAX),
--    @sender_id INT,
--	@recipient_id int,
--	@is_spam int,
--	@is_cc BIT,
--	@is_bcc BIT
--AS
--BEGIN
--	declare @email_id int
	
	
--	select @email_id = (select top 1 email_id from Recipients order by email_id desc)
--	SELECT @is_cc = is_cc , @is_bcc = is_bcc FROM Recipients WHERE email_id = @email_id

--	IF @is_cc =1 OR @is_bcc = 1 and  
--	BEGIN
--		INSERT INTO Recipients (user_id, email_id, is_cc, is_bcc  )
--		VALUES (@recipient_id, @email_id,@is_cc, @is_bcc);
--	END
--		INSERT INTO Emails (subject, body, sender_id, timestamp, is_spam)
--		VALUES (@subject, @body, @sender_id, GETDATE(), @is_spam);
--		INSERT INTO Recipients (user_id, email_id, is_cc, is_bcc )
--		VALUES (@recipient_id, @email_id, @is_cc, @is_bcc);

--END


ALTER PROCEDURE sp_InsertEmail
    @subject NVARCHAR(255),
    @body NVARCHAR(MAX),
    @sender_id INT,
    @recipient_id INT,
    @is_spam INT,
    @is_cc BIT,
    @is_bcc BIT,
    @addEmail BIT
AS
BEGIN
    DECLARE @email_id INT

    IF (@addEmail = 1)    -- Thêm email mới nếu @addEmail = 1 hoặc gửi CC hoặc gửi BCC
    BEGIN
        INSERT INTO Emails (subject, body, sender_id, timestamp, is_spam)
        VALUES (@subject, @body, @sender_id, GETDATE(), @is_spam);

    END
    SELECT @email_id = (select top 1 email_id from Emails order by email_id desc);  -- Lấy ID của email vừa thêm
	
    INSERT INTO Recipients (user_id, email_id, is_cc, is_bcc)
    VALUES (@recipient_id, @email_id, @is_cc, @is_bcc);
END

--ALTER PROCEDURE sp_InsertEmail
--    @subject NVARCHAR(255),	
--    @body NVARCHAR(MAX),
--    @sender_id INT,
--    @recipient_ids INT,  -- Thay thế @recipient_id bằng @recipient_ids (một danh sách các ID người nhận)
--    @is_spam INT,
--    @is_cc BIT,
--    @is_bcc BIT,
--    @addEmail BIT
--AS
--BEGIN
--    DECLARE @email_id INT

--    IF @addEmail = 1
--    BEGIN
--        INSERT INTO Emails (subject, body, sender_id, timestamp, is_spam)
--        VALUES (@subject, @body, @sender_id, GETDATE(), @is_spam);

--        SELECT @email_id = SCOPE_IDENTITY();  -- Lấy ID của email vừa thêm
--    END

--    -- Thêm các bản ghi trong bảng Recipients cho từng người nhận, sử dụng cùng một ID email
--    INSERT INTO Recipients (user_id, email_id, is_cc, is_bcc)
--    SELECT recipient_id, @email_id, @is_cc, @is_bcc
--    FROM SplitIntList(@recipient_ids); -- SplitIntList là một hàm tự tạo để chuyển danh sách ID người nhận thành bảng tạm

--END
go


select top 1 email_id from Emails order by email_id desc






-- này là có spam
select e.timestamp, u.username, e.subject, e.body from Emails e join Recipients r on e.email_id = r.email_id join Users u on u.user_id = e.sender_id where r.user_id = 1 and e.is_spam = 1;


--truy suất hộp thư đếnz
select e.timestamp, r.user_id, e.subject, e.body from Emails e join Recipients r on e.email_id = r.email_id join Users u on u.user_id = e.sender_id where e.sender_id = 2



--truy suất ra hộp thư đã gửi
SELECT e.timestamp,u.username,  e.subject, e.body 
FROM Recipients r
INNER JOIN Users u ON r.user_id = u.user_id
inner join Emails e ON e.email_id = r.email_id
WHERE r.email_id IN (SELECT email_id FROM Emails WHERE sender_id = 1);

--truy suat cc



DBCC CHECKIDENT ('[Emails]', RESEED, 0);
GO

DBCC CHECKIDENT ('[Recipients]', RESEED, 0);
GO