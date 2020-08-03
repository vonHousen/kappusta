package com.vonHousen.kappusta.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

data class Migrations (
    val MIGRATION_5_6: Migration = object : Migration(5, 6) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("""
                    drop table 'BUDGET';
                """)
            database.execSQL("""
                    create table 'BUDGET' (
                        first_day_date  INTEGER not null
                    ,   budget_worth    REAL not null
                    ,   primary key (first_day_date)
                    );
                """)
        }
    },
    val MIGRATION_6_7: Migration = object : Migration(6, 7) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // EXPENSE_TAGS
            database.execSQL("""
                    create table EXPENSE_TAGS (
                        expense_tag_id  INTEGER primary key autoincrement
                    ,   expense_tag     TEXT not null
                    ,   created_date    INTEGER not null
                    );
                """)
            database.execSQL("""
                    alter table EXPENSES rename to TMP_EXPENSES; 
                """)
            database.execSQL("""
                    create table EXPENSES (
                        expense_id        INTEGER PRIMARY KEY AUTOINCREMENT
                    ,   expense_type_id   INTEGER NOT NULL
                    ,   expense_tag_id    INTEGER
                    ,   worth             REAL NOT NULL
                    ,   date              INTEGER NOT NULL
                    ,   comment           TEXT
                    ,   created_date      INTEGER NOT NULL
                    ,   edited_date       INTEGER NOT NULL
                    ,   FOREIGN KEY(expense_type_id) 
                        REFERENCES EXPENSE_TYPES(expense_type_id) 
                        ON UPDATE NO ACTION 
                        ON DELETE CASCADE
                    ,   FOREIGN KEY(expense_tag_id) 
                        REFERENCES EXPENSE_TAGS(expense_tag_id) 
                        ON UPDATE NO ACTION 
                        ON DELETE CASCADE
                    ); 
                """)
            database.execSQL("""
                    create index index_EXPENSES_expense_tag_id ON EXPENSES (expense_tag_id);
                """)

            database.execSQL("""
                    create index index_EXPENSES_expense_type_id ON EXPENSES (expense_type_id);
                """)
            database.execSQL("""
                    insert into EXPENSES
                    (
                        expense_id        
                    ,   expense_type_id   
                    ,   worth             
                    ,   date              
                    ,   created_date      
                    ,   edited_date       
                    )
                    select
                        expense_id        
                    ,   expense_type_id   
                    ,   worth             
                    ,   date              
                    ,   date      
                    ,   date
                    from TMP_EXPENSES;
                """)
            database.execSQL("""
                    drop table TMP_EXPENSES; 
                """)
            // PROFIT_TAGS
            database.execSQL("""
                    create table PROFIT_TAGS (
                        profit_tag_id   INTEGER primary key autoincrement
                    ,   profit_tag      TEXT not null
                    ,   created_date    INTEGER not null
                    );
                """)
            database.execSQL("""
                    alter table PROFITS rename to TMP_PROFITS; 
                """)
            database.execSQL("""
                    create table PROFITS (
                        profit_id         INTEGER PRIMARY KEY AUTOINCREMENT
                    ,   profit_type_id    INTEGER NOT NULL
                    ,   profit_tag_id     INTEGER
                    ,   worth             REAL NOT NULL
                    ,   date              INTEGER NOT NULL
                    ,   comment           TEXT
                    ,   created_date      INTEGER NOT NULL
                    ,   edited_date       INTEGER NOT NULL
                    ,   FOREIGN KEY(profit_tag_id) 
                        REFERENCES PROFIT_TAGS(profit_tag_id) 
                        ON UPDATE NO ACTION 
                        ON DELETE CASCADE
                   ,   FOREIGN KEY(profit_type_id) 
                        REFERENCES PROFIT_TYPES(profit_type_id) 
                        ON UPDATE NO ACTION 
                        ON DELETE CASCADE
                    ); 
                """)
            database.execSQL("""
                    create index index_PROFITS_profit_tag_id ON PROFITS (profit_tag_id);
                """)
            database.execSQL("""
                    create index index_PROFITS_profit_type_id ON PROFITS (profit_type_id);
                """)
            database.execSQL("""
                    insert into PROFITS
                    (
                        profit_id        
                    ,   profit_type_id   
                    ,   worth             
                    ,   date              
                    ,   created_date      
                    ,   edited_date       
                    )
                    select
                        profit_id        
                    ,   profit_type_id   
                    ,   worth             
                    ,   date              
                    ,   date      
                    ,   date
                    from TMP_PROFITS;
                """)
            database.execSQL("""
                    drop table TMP_PROFITS; 
                """)
        }
    }
)