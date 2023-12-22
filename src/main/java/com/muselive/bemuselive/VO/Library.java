package com.muselive.bemuselive.VO;

import lombok.Data;

@Data
public class Library {
    public Integer id;
    public String book_name;
    public String book_barcode;
    public Integer borrower_school_id;
    public String return_datetime;
    public Integer late_fee;
    public Integer complete;
}
