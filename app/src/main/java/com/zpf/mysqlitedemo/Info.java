package com.zpf.mysqlitedemo;

/**
 * Created by ZPF on 2018/4/24.
 */

public class Info {
        private int infoA = 1;
        private String infoB = "info";

    public Info(int infoA, String infoB) {
        this.infoA = infoA;
        this.infoB = infoB;
    }

    public int getInfoA() {
            return infoA;
        }

        public void setInfoA(int infoA) {
            this.infoA = infoA;
        }

        public String getInfoB() {
            return infoB;
        }

        public void setInfoB(String infoB) {
            this.infoB = infoB;
        }
}
