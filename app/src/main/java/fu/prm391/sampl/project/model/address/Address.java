package fu.prm391.sampl.project.model.address;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Address implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("userId")
    @Expose
    private int userId;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("isDefault")
    @Expose
    private int isDefault;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("provinceName")
    @Expose
    private String provinceName;
    @SerializedName("districtName")
    @Expose
    private String districtName;
    @SerializedName("wardName")
    @Expose
    private String wardName;

    @SerializedName("provinceId")
    @Expose
    private int provinceId;

    @SerializedName("districtId")
    @Expose
    private int districtId;

    @SerializedName("wardId")
    @Expose
    private int wardId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public int getWardId() {
        return wardId;
    }

    public void setWardId(int wardId) {
        this.wardId = wardId;
    }

    public Address(int id, int userId, String fullName, String phone, String detail, int isDefault, String createdAt, String updatedAt, String provinceName, String districtName, String wardName, int provinceId, int districtId, int wardId) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
        this.phone = phone;
        this.detail = detail;
        this.isDefault = isDefault;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.provinceName = provinceName;
        this.districtName = districtName;
        this.wardName = wardName;
        this.provinceId = provinceId;
        this.districtId = districtId;
        this.wardId = wardId;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", detail='" + detail + '\'' +
                ", isDefault=" + isDefault +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", districtName='" + districtName + '\'' +
                ", wardName='" + wardName + '\'' +
                ", provinceId=" + provinceId +
                ", districtId=" + districtId +
                ", wardId=" + wardId +
                '}';
    }
}
