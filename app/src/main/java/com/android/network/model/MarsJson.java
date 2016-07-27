package com.android.network.model;

/**
 * Created by cheyanxu on 16/7/27.
 */
public class MarsJson {
    private Report report;

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public class Report {
        private String atmo_opacity;
        private String min_temp;
        private String max_temp;

        public String getAtmo_opacity() {
            return atmo_opacity;
        }

        public void setAtmo_opacity(String atmo_opacity) {
            this.atmo_opacity = atmo_opacity;
        }

        public String getMin_temp() {
            return min_temp;
        }

        public void setMin_temp(String min_temp) {
            this.min_temp = min_temp;
        }

        public String getMax_temp() {
            return max_temp;
        }

        public void setMax_temp(String max_temp) {
            this.max_temp = max_temp;
        }
    }
}
