package com.example.iscreen.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.iscreen.remote.model.DolPhoto;

public class ProduitParcelable implements Parcelable {
    private Long id;
    private String label;
    private String description;
    private String price;
    private String price_ttc;
    private String price_min;
    private String price_min_ttc;
    private String price_base_type;
    private String tva_tx;
    private Integer stock_reel;
    private String stock_theorique;
    private String seuil_stock_alerte;
    private Boolean duration_value;
    private String duration_unit;
    private String weight;
    private String weight_units;
    private String length;
    private String length_units;
    private String surface;
    private String surface_units;
    private String volume;
    private String volume_units;
    private String date_creation;
    private String date_modification;
    private String width;
    private String width_units;
    private String height;
    private String height_units;
    private String ref;
    private String qty;
    private String subprice;
    private String total_ht;
    private String total_tva;
    private String total_ttc;
    private long categorie_id;
    private String note;
    private String note_private;
    private String note_public;
    private String remise;
    private String remise_percent;

    private String local_poster_path;
    private DolPhoto poster;

    public ProduitParcelable() {
    }

    public String getTotal_ht() {
        return total_ht;
    }

    public void setTotal_ht(String total_ht) {
        this.total_ht = total_ht;
    }

    public String getTotal_tva() {
        return total_tva;
    }

    public void setTotal_tva(String total_tva) {
        this.total_tva = total_tva;
    }

    public String getTotal_ttc() {
        return total_ttc;
    }

    public void setTotal_ttc(String total_ttc) {
        this.total_ttc = total_ttc;
    }

    public ProduitParcelable(String label, String price) {
        this.label = label;
        this.price = price;
    }

    public String getSubprice() {
        return subprice;
    }

    public void setSubprice(String subprice) {
        this.subprice = subprice;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice_ttc() {
        return price_ttc;
    }

    public void setPrice_ttc(String price_ttc) {
        this.price_ttc = price_ttc;
    }

    public String getPrice_min() {
        return price_min;
    }

    public void setPrice_min(String price_min) {
        this.price_min = price_min;
    }

    public String getPrice_min_ttc() {
        return price_min_ttc;
    }

    public void setPrice_min_ttc(String price_min_ttc) {
        this.price_min_ttc = price_min_ttc;
    }

    public String getPrice_base_type() {
        return price_base_type;
    }

    public void setPrice_base_type(String price_base_type) {
        this.price_base_type = price_base_type;
    }

    public String getTva_tx() {
        return tva_tx;
    }

    public void setTva_tx(String tva_tx) {
        this.tva_tx = tva_tx;
    }

    public Integer getStock_reel() {
        return stock_reel;
    }

    public void setStock_reel(Integer stock_reel) {
        this.stock_reel = stock_reel;
    }

    public String getStock_theorique() {
        return stock_theorique;
    }

    public void setStock_theorique(String stock_theorique) {
        this.stock_theorique = stock_theorique;
    }

    public String getSeuil_stock_alerte() {
        return seuil_stock_alerte;
    }

    public void setSeuil_stock_alerte(String seuil_stock_alerte) {
        this.seuil_stock_alerte = seuil_stock_alerte;
    }

    public Boolean getDuration_value() {
        return duration_value;
    }

    public void setDuration_value(Boolean duration_value) {
        this.duration_value = duration_value;
    }

    public String getDuration_unit() {
        return duration_unit;
    }

    public void setDuration_unit(String duration_unit) {
        this.duration_unit = duration_unit;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeight_units() {
        return weight_units;
    }

    public void setWeight_units(String weight_units) {
        this.weight_units = weight_units;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getLength_units() {
        return length_units;
    }

    public void setLength_units(String length_units) {
        this.length_units = length_units;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public String getSurface_units() {
        return surface_units;
    }

    public void setSurface_units(String surface_units) {
        this.surface_units = surface_units;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getVolume_units() {
        return volume_units;
    }

    public void setVolume_units(String volume_units) {
        this.volume_units = volume_units;
    }

    public String getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(String date_creation) {
        this.date_creation = date_creation;
    }

    public String getDate_modification() {
        return date_modification;
    }

    public void setDate_modification(String date_modification) {
        this.date_modification = date_modification;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getWidth_units() {
        return width_units;
    }

    public void setWidth_units(String width_units) {
        this.width_units = width_units;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHeight_units() {
        return height_units;
    }

    public void setHeight_units(String height_units) {
        this.height_units = height_units;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getLocal_poster_path() {
        return local_poster_path;
    }

    public void setLocal_poster_path(String local_poster_path) {
        this.local_poster_path = local_poster_path;
    }

    public DolPhoto getPoster() {
        return poster;
    }

    public void setPoster(DolPhoto poster) {
        this.poster = poster;
    }

    public long getCategorie_id() {
        return categorie_id;
    }

    public void setCategorie_id(long categorie_id) {
        this.categorie_id = categorie_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote_private() {
        return note_private;
    }

    public void setNote_private(String note_private) {
        this.note_private = note_private;
    }

    public String getNote_public() {
        return note_public;
    }

    public void setNote_public(String note_public) {
        this.note_public = note_public;
    }

    public String getRemise() {
        return remise;
    }

    public void setRemise(String remise) {
        this.remise = remise;
    }

    public String getRemise_percent() {
        return remise_percent;
    }

    public void setRemise_percent(String remise_percent) {
        this.remise_percent = remise_percent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.label);
        dest.writeString(this.description);
        dest.writeString(this.price);
        dest.writeString(this.price_ttc);
        dest.writeString(this.price_min);
        dest.writeString(this.price_min_ttc);
        dest.writeString(this.price_base_type);
        dest.writeString(this.tva_tx);
        dest.writeValue(this.stock_reel);
        dest.writeString(this.stock_theorique);
        dest.writeString(this.seuil_stock_alerte);
        dest.writeValue(this.duration_value);
        dest.writeString(this.duration_unit);
        dest.writeString(this.weight);
        dest.writeString(this.weight_units);
        dest.writeString(this.length);
        dest.writeString(this.length_units);
        dest.writeString(this.surface);
        dest.writeString(this.surface_units);
        dest.writeString(this.volume);
        dest.writeString(this.volume_units);
        dest.writeString(this.date_creation);
        dest.writeString(this.date_modification);
        dest.writeString(this.width);
        dest.writeString(this.width_units);
        dest.writeString(this.height);
        dest.writeString(this.height_units);
        dest.writeString(this.ref);
        dest.writeString(this.qty);
        dest.writeString(this.subprice);
        dest.writeString(this.total_ht);
        dest.writeString(this.total_tva);
        dest.writeString(this.total_ttc);
        dest.writeLong(this.categorie_id);
        dest.writeString(this.note);
        dest.writeString(this.note_private);
        dest.writeString(this.note_public);
        dest.writeString(this.remise);
        dest.writeString(this.remise_percent);
        dest.writeString(this.local_poster_path);
        dest.writeParcelable(this.poster, flags);
    }

    protected ProduitParcelable(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.label = in.readString();
        this.description = in.readString();
        this.price = in.readString();
        this.price_ttc = in.readString();
        this.price_min = in.readString();
        this.price_min_ttc = in.readString();
        this.price_base_type = in.readString();
        this.tva_tx = in.readString();
        this.stock_reel = (Integer) in.readValue(Integer.class.getClassLoader());
        this.stock_theorique = in.readString();
        this.seuil_stock_alerte = in.readString();
        this.duration_value = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.duration_unit = in.readString();
        this.weight = in.readString();
        this.weight_units = in.readString();
        this.length = in.readString();
        this.length_units = in.readString();
        this.surface = in.readString();
        this.surface_units = in.readString();
        this.volume = in.readString();
        this.volume_units = in.readString();
        this.date_creation = in.readString();
        this.date_modification = in.readString();
        this.width = in.readString();
        this.width_units = in.readString();
        this.height = in.readString();
        this.height_units = in.readString();
        this.ref = in.readString();
        this.qty = in.readString();
        this.subprice = in.readString();
        this.total_ht = in.readString();
        this.total_tva = in.readString();
        this.total_ttc = in.readString();
        this.categorie_id = in.readLong();
        this.note = in.readString();
        this.note_private = in.readString();
        this.note_public = in.readString();
        this.remise = in.readString();
        this.remise_percent = in.readString();
        this.local_poster_path = in.readString();
        this.poster = in.readParcelable(DolPhoto.class.getClassLoader());
    }

    public static final Creator<ProduitParcelable> CREATOR = new Creator<ProduitParcelable>() {
        @Override
        public ProduitParcelable createFromParcel(Parcel source) {
            return new ProduitParcelable(source);
        }

        @Override
        public ProduitParcelable[] newArray(int size) {
            return new ProduitParcelable[size];
        }
    };
}
