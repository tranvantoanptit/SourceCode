DROP TABLE IF EXISTS tmp;
SELECT "expensecode" || ':' || "expensename" AS expense, to_char(to_date(targetyearmonth,'yyyy_MM'),'YYYYMM') as targetyearmonth, SUM(expensevalue) AS result_sum
FROM result WHERE orgcode IN ('84A61110','84A61130','84A61210','84A61220') 
AND expensecode IN ('2152','2153','2311','2312','2315','2316','2381') 
AND makedate IN (SELECT DISTINCT max(makedate) FROM result WHERE targetyearmonth BETWEEN '2016/05' AND '2016/07' 
GROUP BY targetyearmonth, orgcode ORDER BY 1 ASC) GROUP BY 1,2 ORDER BY 1,2 ASC;