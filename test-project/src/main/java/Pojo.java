import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

/**
 * Created by vi34 on 13/08/16.
 */
@Getter
public class Pojo {
    int a;
    String s;
    List<Integer> list;

    public Pojo(int a, String s, List<Integer> list) {
        this.a = a;
        this.s = s;
        this.list = list;
    }

    @JsonProperty("A")
    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    @JsonProperty("S")
    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    @JsonProperty("Flist")
    public List<Integer> getList() {
        return list;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }
}
