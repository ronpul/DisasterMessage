package network.model;

import com.google.gson.annotations.SerializedName;

public class Member {
    @SerializedName("id") public String id;
    @SerializedName("age") public int age;
    @SerializedName("email") public String email;
}
