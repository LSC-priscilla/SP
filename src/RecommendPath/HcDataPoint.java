package RecommendPath;

class HcDataPoint {
    private HcCluster hcCluster;
    private String data;
    private String dataPointName;

    public HcDataPoint(String data) {
        this.data = data;
    }

    public HcDataPoint(String data, String dataPointName) {
        this.data = data;
        this.dataPointName = dataPointName;
    }

    public HcDataPoint() {

    }

    public String getDataPointName() {
        return dataPointName;
    }

    public HcCluster getHcCluster() {
        return hcCluster;
    }

    public void setHcCluster(HcCluster hcCluster) {
        this.hcCluster = hcCluster;
    }

    public String getData() {
        return data;
    }
}