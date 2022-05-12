package com.example.sparks.nonentity;

import java.util.List;
import java.util.Map;

import com.example.sparks.embeddable.RepublicanDemocratSplit;
import com.example.sparks.enumerable.PoliticalGroup;

/**
 * Contains the different calculated values for the 10,000 randomly generated districting
 * plans by the SeaWulf supercomputer
 */
public class SeawulfSummary {
    private Map<PoliticalGroup,List<BoxAndWhiskerData>> boxAndWhiskerData;
    private Map<PoliticalGroup, Map<Integer, Integer>> majorityMinorityRange;

    // @TODO discuss this (along with @TODO in State.java)
    private List<RepublicanDemocratSplit> republicanDemocratSplit;
    // private Map<String, Integer> republicanDemocratSplit;

    private SeatShareData seatShareData;

    /**
     * @return Map<PoliticalGroup,List<BoxAndWhiskerData>> return the boxAndWhiskerData
     */
    public Map<PoliticalGroup,List<BoxAndWhiskerData>> getBoxAndWhiskerData() {
        return boxAndWhiskerData;
    }

    /**
     * @param boxAndWhiskerData the boxAndWhiskerData to set
     */
    public void setBoxAndWhiskerData(Map<PoliticalGroup,List<BoxAndWhiskerData>> boxAndWhiskerData) {
        this.boxAndWhiskerData = boxAndWhiskerData;
    }

    /**
     * @return Map<PoliticalGroup, Map<Integer, Integer>> return the majorityMinorityRange
     */
    public Map<PoliticalGroup, Map<Integer, Integer>> getMajorityMinorityRange() {
        return majorityMinorityRange;
    }

    /**
     * @param majorityMinorityRange the majorityMinorityRange to set
     */
    public void setMajorityMinorityRange(Map<PoliticalGroup, Map<Integer, Integer>> majorityMinorityRange) {
        this.majorityMinorityRange = majorityMinorityRange;
    }


    /**
     * @return List<RepublicanDemocratSplit> return the republicanDemocratSplit
     */
    public List<RepublicanDemocratSplit> getRepublicanDemocratSplit() {
        return republicanDemocratSplit;
    }

    /**
     * @param republicanDemocratSplit the republicanDemocratSplit to set
     */
    public void setRepublicanDemocratSplit(List<RepublicanDemocratSplit> republicanDemocratSplit) {
        this.republicanDemocratSplit = republicanDemocratSplit;
    }

    /**
     * @return SeatShareData return the seatShareData
     */
    public SeatShareData getSeatShareData() {
        return seatShareData;
    }

    /**
     * @param seatShareData the seatShareData to set
     */
    public void setSeatShareData(SeatShareData seatShareData) {
        this.seatShareData = seatShareData;
    }
}
