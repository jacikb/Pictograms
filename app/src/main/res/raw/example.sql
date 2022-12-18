https://www.tutorialspoint.com/execute_sql_online.php

create table PICTOGRAM_TABLE (id int, parent int, role int, name varchar(20));

    insert into PICTOGRAM_TABLE values(1,0,0,'A');
    insert into PICTOGRAM_TABLE values(2,0,0,'B');
    insert into PICTOGRAM_TABLE values(3,0,0,'Dir');
    insert into PICTOGRAM_TABLE values(4,3,0,'D A');
    insert into PICTOGRAM_TABLE values(5,3,0,'D B');

    select id, parent ,name , role  from PICTOGRAM_TABLE


UPDATE inventory
   SET quantity = quantity - daily.amt
  FROM (SELECT sum(quantity) AS amt, itemId FROM sales GROUP BY 2) AS daily
 WHERE inventory.itemId = daily.itemId;


     UPDATE PICTOGRAM_TABLE
     SET role = s.quantity
     FROM (SELECT count(id) AS quantity, parent.id  FROM PICTOGRAM_TABLE GROUP BY 2) AS s
     WHERE PICTOGRAM_TABLE.id = s.parent_id;