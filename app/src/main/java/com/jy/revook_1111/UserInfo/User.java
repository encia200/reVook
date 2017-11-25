package com.jy.revook_1111.UserInfo;

public class User {
    private String id;
    private String pwd;
    private String name;
    private String profile_image;
    private String nickname;
    private String tel;
    private String gender;

    public User(String id, String pwd, String name, String profile_image, String nickname, String tel, String gender) {
        this.id = id;
        this.pwd = pwd;
        this.name = name;
        this.profile_image = profile_image;
        this.nickname = nickname;
        this.tel = tel;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public String getPwd() {
        return pwd;
    }

    public String getName() {
        return name;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public String getNickname() {
        return nickname;
    }

    public String getTel() {
        return tel;
    }

    public String getGender() {
        return gender;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!id.equals(user.id)) return false;
        if (!pwd.equals(user.pwd)) return false;
        if (!name.equals(user.name)) return false;
        if (profile_image != null ? !profile_image.equals(user.profile_image) : user.profile_image != null)
            return false;
        if (!nickname.equals(user.nickname)) return false;
        if (tel != null ? !tel.equals(user.tel) : user.tel != null) return false;
        return gender.equals(user.gender);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + pwd.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (profile_image != null ? profile_image.hashCode() : 0);
        result = 31 * result + nickname.hashCode();
        result = 31 * result + (tel != null ? tel.hashCode() : 0);
        result = 31 * result + gender.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", pwd='" + pwd + '\'' +
                ", name='" + name + '\'' +
                ", profile_image='" + profile_image + '\'' +
                ", nickname='" + nickname + '\'' +
                ", tel='" + tel + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }

}