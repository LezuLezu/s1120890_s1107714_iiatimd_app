package com.iatjrd.geld_lenen_app;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LoanDAO {

    @Query("SELECT * FROM loan")
    List<Loan> getAll();

    @Insert
    void InsertLoan(Loan loan);

    @Delete
    void delete(Loan loan);
}
