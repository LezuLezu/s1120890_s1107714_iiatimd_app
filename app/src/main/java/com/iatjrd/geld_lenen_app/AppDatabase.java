package com.iatjrd.geld_lenen_app;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Loan.class}, version = 14)
public abstract class AppDatabase extends RoomDatabase{
    public abstract LoanDAO loanDAO();

    private static AppDatabase instance;

    static synchronized AppDatabase getInstance(Context context){
        if(instance == null){
            instance = create(context);
        }
        return instance;
    }

    private static AppDatabase create(final Context context){
        return Room.databaseBuilder(context, AppDatabase.class, "loans").allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }
}
