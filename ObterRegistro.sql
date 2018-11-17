Alter PROCEDURE ObterRegistro
AS

DECLARE @TMP_TABLE table(  
	id int
);  

UPDATE TOP (1) testes
set status = 1 

OUTPUT inserted.id
INTO @TMP_TABLE

where status <> 1 or status is null

select * from @TMP_TABLE

GO