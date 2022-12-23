package top.huanyv.jdbc.mybatis.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Book {

    private Integer id;
    private String bname;
    private String author;
    private String pubcomp;
    private Date pubdate;
    private Integer bcount;
    private BigDecimal price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPubcomp() {
        return pubcomp;
    }

    public void setPubcomp(String pubcomp) {
        this.pubcomp = pubcomp;
    }

    public Date getPubdate() {
        return pubdate;
    }

    public void setPubdate(Date pubdate) {
        this.pubdate = pubdate;
    }

    public Integer getBcount() {
        return bcount;
    }

    public void setBcount(Integer bcount) {
        this.bcount = bcount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", bname='" + bname + '\'' +
                ", author='" + author + '\'' +
                ", pubcomp='" + pubcomp + '\'' +
                ", pubdate=" + pubdate +
                ", bcount=" + bcount +
                ", price=" + price +
                '}';
    }
}
