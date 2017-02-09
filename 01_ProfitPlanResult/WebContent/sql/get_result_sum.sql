select * into temp table t1  from crosstab (
 'select Expense,targetyearmonth,result_sum from tmp group by 1,2,3 order by 1,2',
 'select distinct targetyearmonth from tmp order by 1'
 )
 as newtable (
 Expense varchar,"2016/05" numeric,"2016/06" numeric,"2016/07" numeric
 );
select * into temp table t2  from crosstab (
 'select targetyearmonth,Expense,result_sum from tmp group by 1,2,3 order by 1,2',
 'select distinct Expense from tmp order by 1'
 )
 as newtable (
 targetyearmonth varchar,"2152:RAW CERAMIC AND PARTS SALES" numeric,"2154:FIRING SALES" numeric,"2155:PLATING SALES" numeric,"2312:AUXILIARY MATERIAL USAGE" numeric,"2381:GAS & FUEL" numeric,"2382:POWER & WATER" numeric
 );
select * from crosstab (
 'select targetyearmonth,Expense,result_sum from tmp group by 1,2,3 order by 1,2',
 'select distinct Expense from tmp order by 1'
 )
 as newtable (
 targetyearmonth varchar,"2152:RAW CERAMIC AND PARTS SALES" numeric,"2154:FIRING SALES" numeric,"2155:PLATING SALES" numeric,"2312:AUXILIARY MATERIAL USAGE" numeric,"2381:GAS & FUEL" numeric,"2382:POWER & WATER" numeric
 );
SELECT array_to_json(array_agg(row_to_json(tc1))) FROM (select * from t1) tc1;
SELECT array_agg(tc2)  FROM (SELECT column_name FROM information_schema.columns WHERE table_name ='t2') tc2;