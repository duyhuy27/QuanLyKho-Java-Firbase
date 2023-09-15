package team1XuongMobile.fpoly.myapplication.models;

public class DateRangeProfit {
    private String date;
    private double profit;

    public DateRangeProfit(String date, double profit) {
        this.date = date;
        this.profit = profit;
    }

    public String getDate() {
        return date;
    }

    public double getProfit() {
        return profit;
    }
}
