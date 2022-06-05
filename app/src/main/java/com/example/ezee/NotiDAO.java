package com.example.ezee;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotiDAO {

    // TODO: Change Notification Limit here
    @Query("SELECT * FROM NotiClass LIMIT 10")
    List<NotiClass> getallNoti();

    @Insert
    void insert(NotiClass notifications);

    @Query("DELETE FROM NotiClass WHERE uid=:uid")
    void deleteByUID(int uid);
}
