package com.mywidget;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Util {

    public static int loveDay(int year, int month, int date) {

        GregorianCalendar today = new GregorianCalendar ( );

        int nYear1 = today.get(today.YEAR);
        int nMonth1 = today.get(today.MONTH) + 1;
        int nDate1 = today.get(today.DATE);

        int nYear2 = year;
        int nMonth2 = month;
        int nDate2 = date;

        Calendar cal = Calendar.getInstance();
        int nTotalDate1 = 0, nTotalDate2 = 0, nDiffOfYear = 0, nDiffOfDay = 0;

        if ( nYear1 > nYear2 ) {
            for ( int i = nYear2; i < nYear1; i++ ) {
                cal.set ( i, 12, 0 ); nDiffOfYear += cal.get ( Calendar.DAY_OF_YEAR );
            }
            nTotalDate1 += nDiffOfYear;
        } else if ( nYear1 < nYear2 ) {
            for ( int i = nYear1; i < nYear2; i++ ) {
                cal.set ( i, 12, 0 );
                nDiffOfYear += cal.get ( Calendar.DAY_OF_YEAR );
            }
            nTotalDate2 += nDiffOfYear;
        }
        cal.set ( nYear1, nMonth1-1, nDate1 );
        nDiffOfDay = cal.get ( Calendar.DAY_OF_YEAR );
        nTotalDate1 += nDiffOfDay;
        cal.set ( nYear2, nMonth2-1, nDate2 );
        nDiffOfDay = cal.get ( Calendar.DAY_OF_YEAR );
        nTotalDate2 += nDiffOfDay;

        return nTotalDate1-nTotalDate2+1;
    }

    public static int dDay(int year, int month, int day) {

        GregorianCalendar today = new GregorianCalendar ( );

        int nYear1 = year;
        int nMonth1 = month;
        int nDate1 = day;

        int nYear2 = today.get(today.YEAR);
        int nMonth2 = today.get(today.MONTH) + 1;
        int nDate2 = today.get(today.DATE);

        Calendar cal = Calendar.getInstance();
        int nTotalDate1 = 0, nTotalDate2 = 0, nDiffOfYear = 0, nDiffOfDay = 0;

        if ( nYear1 > nYear2 ) {
            for ( int i = nYear2; i < nYear1; i++ ) {
                cal.set ( i, 12, 0 ); nDiffOfYear += cal.get ( Calendar.DAY_OF_YEAR );
            }
            nTotalDate1 += nDiffOfYear;
        } else if ( nYear1 < nYear2 ) {
            for ( int i = nYear1; i < nYear2; i++ ) {
                cal.set ( i, 12, 0 );
                nDiffOfYear += cal.get ( Calendar.DAY_OF_YEAR );
            }
            nTotalDate2 += nDiffOfYear;
        }
        cal.set ( nYear1, nMonth1-1, nDate1 );
        nDiffOfDay = cal.get ( Calendar.DAY_OF_YEAR );
        nTotalDate1 += nDiffOfDay;
        cal.set ( nYear2, nMonth2-1, nDate2 );
        nDiffOfDay = cal.get ( Calendar.DAY_OF_YEAR );
        nTotalDate2 += nDiffOfDay;

        return nTotalDate1-nTotalDate2;
    }
}
