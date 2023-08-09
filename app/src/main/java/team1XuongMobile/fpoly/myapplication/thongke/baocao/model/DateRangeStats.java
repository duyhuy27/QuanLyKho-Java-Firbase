package team1XuongMobile.fpoly.myapplication.thongke.baocao.model;

public class DateRangeStats {
    private String date;
    private int totalInvoiceCount;
    private double totalValue;

    public DateRangeStats(String date, int totalInvoiceCount, double totalValue) {
        this.date = date;
        this.totalInvoiceCount = totalInvoiceCount;
        this.totalValue = totalValue;
    }

    public String getDate() {
        return date;
    }

    public int getTotalInvoiceCount() {
        return totalInvoiceCount;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void addToInvoiceCount(int count) {
        totalInvoiceCount += count;
    }

    public void addToTotalValue(double value) {
        totalValue += value;
    }
}

