package com.example.abhishek.shopify;

public class GetSet {

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_Qty() {
        return _Qty;
    }

    public void set_Qty(String _Qty) {
        this._Qty = _Qty;
    }

    public String get_val() {
        return _val;
    }

    public void set_val(String _val) {
        this._val = _val;
    }

    private String _name;
    private String _Qty;
    private String _val;

    public Integer get_measure() {
        return _measure;
    }

    public void set_measure(Integer _measure) {
        this._measure = _measure;
    }

    private Integer _measure;

    public String get_checkedVal() {
        return _checkedVal;
    }

    public void set_checkedVal(String _checkedVal) {
        this._checkedVal = _checkedVal;
    }

    private String _checkedVal;

    public GetSet(String _name, String _Qty, String _val,String _checkedVal,Integer _measure) {
        this._name = _name;
        this._Qty = _Qty;
        this._val = _val;
        this._checkedVal=_checkedVal;
        this._measure=_measure;
    }
}
