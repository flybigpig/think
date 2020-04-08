# android sqlite3

> C:\Users\czdxn>adb shell
> root@aosp:/ # cd /data/data/com.hot.people.experience/databases/
> root@aosp:/data/data/com.hot.people.experience/databases # ls
> ITEMSTORE.db
> ITEMSTORE.db-journal
> qlite3 ITEMSTORE.db                                                           <
> SQLite version 3.8.6.1 2015-05-21 17:24:32
> Enter ".help" for usage hints.
> sqlite> .table
> Item              Items             android_metadata
> sqlite> select * from Item
>    ...> ;
> sqlite> select * from Items
>    ...> ;
> 0|0.5513629050866861|0
> 1|0.34864374291520495|0
> 2|0.49282009401525395|0
> 3|0.515973832779851|0
> 4|0.2968892529833549|0
> 5|0.8249211748330331|0
> 6|0.8596257281791496|0
> 7|0.6907141127480305|0
> 8|0.540853458879361|0
> 9|0.14505295688872277|0
> 10|0.9066406160930313|0
> 11|0.8418543484321573|0
> 12|0.9437114439153554|0
> 13|0.6689556079441252|0
> 14|0.5195496287255769|0
> sqlite> select * from item
>    ...> ;
> 1|0|2020/03/31 16:13:06
> sqlite> select sum(data)  from Item where ids =0
>    ...> ;
> 2020.0
> sqlite> select sum(*) from Item where ids =0
>    ...> ;
> Error: wrong number of arguments to function sum()
> sqlite> select * from Item group by ids
>    ...> ;
> 1|0|2020/03/31 16:13:06
> 2|4|2020/03/31 16:14:27
> sqlite> select * from Item group by ids
>    ...> ;
> 5|0|2020/03/31 16:15:36
> 2|4|2020/03/31 16:14:27
> sqlite> select * from Item group by ids
>    ...> ;
> 5|0|2020/03/31 16:15:36
> 9|3|2020/03/31 16:17:21
> 2|4|2020/03/31 16:14:27
> sqlite> select �
> sqlite> ;
> sqlite> select * from Items
>    ...> ;
> 0|0.5513629050866861|0
> 1|0.34864374291520495|0
> 2|0.49282009401525395|0
> 3|0.515973832779851|0
> 4|0.2968892529833549|0
> 5|0.8249211748330331|0
> 6|0.8596257281791496|0
> 7|0.6907141127480305|0
> 8|0.540853458879361|0
> 9|0.14505295688872277|0
> 10|0.9066406160930313|0
> 11|0.8418543484321573|0
> 12|0.9437114439153554|0
> 13|0.6689556079441252|0
> 14|0.5195496287255769|0
> sqlite> select * from Item where ids =0
>    ...> ;
> 1|0|2020/03/31 16:13:06
> 3|0|2020/03/31 16:15:35
> 4|0|2020/03/31 16:15:35
> 5|0|2020/03/31 16:15:36
> sqlite> select sum() from Item where ids = 0
>    ...> ;
> Error: wrong number of arguments to function sum()
> sqlite> .table
> Item              Items             android_metadata
> sqlite> select * from Items
>    ...> ;
> 0|0.5513629050866861|0
> 1|0.34864374291520495|0
> 2|0.49282009401525395|0
> 3|0.515973832779851|0
> 4|0.2968892529833549|0
> 5|0.8249211748330331|0
> 6|0.8596257281791496|0
> 7|0.6907141127480305|0
> 8|0.540853458879361|0
> 9|0.14505295688872277|0
> 10|0.9066406160930313|0
> 11|0.8418543484321573|0
> 12|0.9437114439153554|0
> 13|0.6689556079441252|0
> 14|0.5195496287255769|0