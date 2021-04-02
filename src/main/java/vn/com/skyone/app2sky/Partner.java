package vn.com.skyone.app2sky;

public class Partner {

    public Partner() {
        super();
    }

    private Long id = 0L;
    private String code = "";
    private String name = "";
    private Integer typeMedic = 0;
    private String firsName = "";
    private String middleName = "";
    private String lastName = "";
    private String codeName = "";
    private String nickName = "";
    private String searchText = "";
    private String title = "";
    private String address = "";
    private String phone = "";
    private String email = "";
    private Integer sex = 0;
    private long age = 0;
    private byte[] digitalsignature = null;
    private Long createdBy = 0L;
    private Long createdDate = 0L;
    
    public Partner(String code) {
        super();
        this.code = code;
    }
    
    public Partner(String code, String name) {
        super();
        this.code = code;
        this.name = name;
    }

    public Partner(Long id, String code, String name, Integer typeMedic, String firsName, String middleName, String lastName,
            String codeName, String nickName, String searchText, String title, String address, String phone,
            String email, Integer sex, long age, byte[] digitalsignature, Long createdBy, Long createdDate) {
        super();
        this.id = id;
        this.code = code;
        this.name = name;
        this.typeMedic = typeMedic;
        this.firsName = firsName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.codeName = codeName;
        this.nickName = nickName;
        this.searchText = searchText;
        this.title = title;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.sex = sex;
        this.age = age;
        this.digitalsignature = digitalsignature;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTypeMedic() {
        return typeMedic;
    }

    public void setTypeMedic(Integer typeMedic) {
        this.typeMedic = typeMedic;
    }

    public String getFirsName() {
        return firsName;
    }

    public void setFirsName(String firsName) {
        this.firsName = firsName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public byte[] getDigitalsignature() {
        return digitalsignature;
    }

    public void setDigitalsignature(byte[] digitalsignature) {
        this.digitalsignature = digitalsignature;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

}
