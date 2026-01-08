CREATE TABLE [Tôm] (
	[id] INTEGER NOT NULL IDENTITY UNIQUE,
	[ten] NVARCHAR(255),
	[tinhchat] INTEGER,
	PRIMARY KEY([id])
);
GO

CREATE TABLE [Tinh chat] (
	[id] INTEGER NOT NULL IDENTITY UNIQUE,
	[ten_tinh_chat] NVARCHAR(255),
	PRIMARY KEY([id])
);
GO

CREATE TABLE [Khach hang] (
	[id] INTEGER NOT NULL IDENTITY UNIQUE,
	[Ho ten] NVARCHAR(255),
	[SDT] VARCHAR(255),
	[Dia chi] NVARCHAR(255),
	PRIMARY KEY([id])
);
GO

CREATE TABLE [Nhap hang] (
	[id] INTEGER NOT NULL IDENTITY UNIQUE,
	[Ngay nhap hang] DATETIME,
	[Nha cung cap] INTEGER,
	[Tong tien hang] DOUBLE,
	[Trang thai thanh toan] NVARCHAR(255),
	[Ghi chu] TEXT(65535),
	PRIMARY KEY([id])
);
GO

CREATE TABLE [CT nhap hang] (
	[id] INTEGER NOT NULL IDENTITY UNIQUE,
	[so luong nhap] FLOAT,
	[Tom] INTEGER,
	[Ma nhap hang] INTEGER,
	[Gia nhap] FLOAT,
	[DVT] NVARCHAR(255),
	PRIMARY KEY([id])
);
GO

CREATE TABLE [Nha cung cap] (
	[id] INTEGER NOT NULL IDENTITY UNIQUE,
	[Ho ten] NVARCHAR(255),
	[SDT] VARCHAR(255),
	[Dia chi] NVARCHAR(255),
	PRIMARY KEY([id])
);
GO

CREATE TABLE [Xuat hang] (
	[id] INTEGER NOT NULL IDENTITY UNIQUE,
	[Ngay xuat] DATETIME,
	[Khach hang] INTEGER,
	[Tong thanh toan] DOUBLE,
	[Hinh thuc thanh toan] NVARCHAR(255),
	PRIMARY KEY([id])
);
GO

CREATE TABLE [CT xuat hang] (
	[id] INTEGER NOT NULL IDENTITY UNIQUE,
	[So luong thuc giao] FLOAT,
	[Ma xuat hang] INTEGER,
	[So luong tra lai] FLOAT,
	[Don gia] FLOAT,
	[Muc khau tru] INTEGER,
	[Ma lo hang] INTEGER,
	[Thanh tien] DOUBLE,
	[Ly do tra lai] TEXT(65535),
	PRIMARY KEY([id])
);
GO

CREATE TABLE [Lo hang] (
	[id] INTEGER NOT NULL IDENTITY UNIQUE,
	[Ma nhap hang] INTEGER,
	[Tom] INTEGER,
	[Tong so luong nhap] FLOAT,
	[Trang thai] NVARCHAR(255),
	PRIMARY KEY([id])
);
GO


ALTER TABLE [Tôm]
ADD FOREIGN KEY([tinhchat])
REFERENCES [Tinh chat]([id])
ON UPDATE NO ACTION ON DELETE NO ACTION;
GO
ALTER TABLE [Nhap hang]
ADD FOREIGN KEY([Nha cung cap])
REFERENCES [Nha cung cap]([id])
ON UPDATE NO ACTION ON DELETE NO ACTION;
GO
ALTER TABLE [CT nhap hang]
ADD FOREIGN KEY([Tom])
REFERENCES [Tôm]([id])
ON UPDATE NO ACTION ON DELETE NO ACTION;
GO
ALTER TABLE [CT nhap hang]
ADD FOREIGN KEY([Ma nhap hang])
REFERENCES [Nhap hang]([id])
ON UPDATE NO ACTION ON DELETE NO ACTION;
GO
ALTER TABLE [Xuat hang]
ADD FOREIGN KEY([Khach hang])
REFERENCES [Khach hang]([id])
ON UPDATE NO ACTION ON DELETE NO ACTION;
GO
ALTER TABLE [CT xuat hang]
ADD FOREIGN KEY([Ma xuat hang])
REFERENCES [Xuat hang]([id])
ON UPDATE NO ACTION ON DELETE NO ACTION;
GO
ALTER TABLE [Lo hang]
ADD FOREIGN KEY([Tom])
REFERENCES [Tôm]([id])
ON UPDATE NO ACTION ON DELETE NO ACTION;
GO
ALTER TABLE [CT xuat hang]
ADD FOREIGN KEY([Ma lo hang])
REFERENCES [Lo hang]([id])
ON UPDATE NO ACTION ON DELETE NO ACTION;
GO
ALTER TABLE [Lo hang]
ADD FOREIGN KEY([Ma nhap hang])
REFERENCES [CT nhap hang]([id])
ON UPDATE NO ACTION ON DELETE NO ACTION;
GO