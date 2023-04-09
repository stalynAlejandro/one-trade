package com.pagonxt.onetradefinance.work.service.model.elastic;

/**
 * model class with tools for elastic responses
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class Shards {

    //class attributes
    private Integer total;

    private Integer successful;

    private Integer skipped;

    private Integer failed;

    /**
     * getter method
     * @return : an Integer with total value
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * setter method
     * @param total : an Integer with total value
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * getter method
     * @return : an Integer with successful value
     */
    public Integer getSuccessful() {
        return successful;
    }

    /**
     * setter method
     * @param successful : an Integer with successful value
     */
    public void setSuccessful(Integer successful) {
        this.successful = successful;
    }

    /**
     * getter method
     * @return : an Integer with skipped value
     */
    public Integer getSkipped() {
        return skipped;
    }

    /**
     * setter method
     * @param skipped : an Integer with skipped value
     */
    public void setSkipped(Integer skipped) {
        this.skipped = skipped;
    }

    /**
     * getter method
     * @return : an Integer with failed value
     */
    public Integer getFailed() {
        return failed;
    }

    /**
     * setter method
     * @param failed : an Integer with failed value
     */
    public void setFailed(Integer failed) {
        this.failed = failed;
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "Shards{" +
                "total=" + total +
                ", successful=" + successful +
                ", skipped=" + skipped +
                ", failed=" + failed +
                '}';
    }
}
