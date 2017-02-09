DROP TABLE IF EXISTS temp1;
SELECT * INTO temp1 FROM (SELECT f1,f2,f5,f6,f13,f19,f28,f3,f31,f36,f55 FROM pktf403 WHERE f40='0' AND f2='50' AND f5 <> '') AS t;

INSERT INTO temp1 SELECT f1,f3,f14,f15,f18,f23,f29,f35,f7,f33,f42 FROM pktf602 WHERE f3='41';

SELECT array_to_json(array_agg(row_to_json(tc1))) FROM (SELECT DISTINCT t2.f55 as data_source, t1.last_dt, t2.f2 as slip_cl, 
t1.f5 as item_cd, t2.f6 as desc, t2.f13 as spec,t2.f28 as unit, t2.f19 as unit_price, t2.f3 as  po_no,t2.f31 as org_cd, t2.f36 as account_cd
FROM (SELECT f5, MAX(TO_DATE(f1,'YYYY/MM/DD')) AS last_dt FROM temp1 GROUP BY 1) t1 
INNER JOIN temp1 t2 
ON t1.f5=t2.f5 AND t1.last_dt=TO_DATE(t2.f1,'YYYY/MM/DD') ORDER BY 2) tc1;