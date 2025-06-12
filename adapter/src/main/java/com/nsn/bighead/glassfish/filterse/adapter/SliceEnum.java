package com.nsn.bighead.glassfish.filterse.adapter;


import java.util.HashMap;
import java.util.Map;

public enum SliceEnum {

    /**
     * slice of 1 min
     */
    SLICE_1MIN(60,'1'),
    /**
     * slice of 5 min
     */
    SLICE_5MIN(5*60,'5'),
    /**
     * slice of 10 min
     */
    SLICE_10MIN(10*60,'T'),
    /**
     * slice of 15 min
     */
    SLICE_15MIN(15*60,'Q'),
    /**
     * slice of 30 min
     */
    SLICE_30MIN(30*60,'F'),
    /**
     * slice of 60 min
     */
    SLICE_60MIN(60*60,'H'),
    /**
     * slice of DAY
     */
    SLICE_DAY(SLICE_60MIN.getValue()*24,'D'),
    /**
     * slice of week
     */
    SLICE_WEEK(SLICE_DAY.getValue()*7,'W'),
    /**
     * slice of month
     */
    SLICE_MONTH(SLICE_DAY.getValue()*30,'M'),
    /**
     * slice of year
     */
    SLICE_YEAR(SLICE_DAY.getValue()*365,'Y');

    private final int value;

    private final char shortName;

    static private Map<Character,Integer> nameValueMap;

    static private Map<Integer,String> valueNameMap;

    SliceEnum(int value,char shortName) {
        this.value = value;
        this.shortName = shortName;
        getNameValueMap().put(shortName, value);
    }

    static private Map<Character,Integer> getNameValueMap(){
        if(nameValueMap==null){
            nameValueMap  = new HashMap<Character,Integer>();
        }
        return nameValueMap;
    }
    static private Map<Integer,String> getValueNameMap(){
        if(valueNameMap == null){
            valueNameMap = new HashMap<Integer,String>();
        }
        return valueNameMap;
    }

    static public Integer searchSliceTypeByName(String sliceTypeName) throws Exception{
        Integer sliceType = getNameValueMap().get(sliceTypeName.toCharArray()[0]);
        if(sliceType==null){
            throw new Exception("can not find slice for sliceType Name:"+sliceTypeName);
        }
        return sliceType;
    }

    static public String searchSliceTypeByValue(int sliceType) throws Exception{
        String res = getValueNameMap().get(sliceType);
        if(res==null){
            throw new Exception("can not find slice for sliceType:"+sliceType);
        }
        return res;
    }

    /**
     * The enumerating value
     */
    public int getValue() {
        return value;
    }

    public char getShortName() {
        return shortName;
    }

    static public void main(String[] arg) throws Exception{
        System.out.println(SliceEnum.searchSliceTypeByName("D"));
        System.out.println(SLICE_60MIN.getValue());
    }

}

